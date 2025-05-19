package com.everyonewaiter.application.auth;

import com.everyonewaiter.application.auth.request.AuthWrite;
import com.everyonewaiter.application.auth.response.TokenResponse;
import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.entity.AuthCode;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.domain.auth.entity.AuthSuccess;
import com.everyonewaiter.domain.auth.entity.RefreshToken;
import com.everyonewaiter.domain.auth.event.AuthCodeSendEvent;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.domain.auth.repository.RefreshTokenRepository;
import com.everyonewaiter.domain.auth.service.AuthValidator;
import com.everyonewaiter.global.exception.AuthenticationException;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.security.JwtPayload;
import com.everyonewaiter.global.security.JwtProvider;
import jakarta.persistence.OptimisticLockException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

  private final JwtProvider jwtProvider;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final AuthValidator authValidator;
  private final AuthRepository authRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  public void checkExistsAuthSuccess(String phoneNumber, AuthPurpose purpose) {
    AuthSuccess authSuccess = new AuthSuccess(phoneNumber, purpose);
    authValidator.checkExistsAuthSuccess(authSuccess);
  }

  public void sendAuthCode(AuthWrite.SendAuthCode request) {
    AuthAttempt authAttempt = new AuthAttempt(request.phoneNumber(), request.purpose());
    authValidator.validateAuthAttempt(authAttempt);

    AuthCode authCode = new AuthCode(request.phoneNumber());
    authRepository.save(authCode);
    authRepository.increment(authAttempt);
    authRepository.delete(new AuthSuccess(request.phoneNumber(), request.purpose()));

    AuthCodeSendEvent event = new AuthCodeSendEvent(request.phoneNumber(), authCode.code());
    applicationEventPublisher.publishEvent(event);
  }

  public void verifyAuthCode(AuthWrite.VerifyAuthCode request) {
    AuthSuccess authSuccess = new AuthSuccess(request.phoneNumber(), request.purpose());
    authValidator.checkNotExistsAuthSuccess(authSuccess);

    AuthCode authCode = new AuthCode(request.phoneNumber(), request.code());
    int code = authRepository.find(authCode);
    authCode.verify(code);

    authRepository.save(authSuccess);
    authRepository.delete(authCode);
  }

  @Transactional
  public void sendAuthMail(String email) {
    applicationEventPublisher.publishEvent(new AuthMailSendEvent(email));
  }

  public String verifyAuthMail(String token) {
    JwtPayload payload = jwtProvider.decode(token)
        .orElseThrow(() -> new BusinessException(ErrorCode.EXPIRED_VERIFICATION_EMAIL));
    authValidator.validateAuthMailTokenPayload(payload);
    return payload.subject();
  }

  public String generateToken(JwtPayload payload, Duration expiration) {
    return jwtProvider.generate(payload, expiration);
  }

  @Transactional
  public TokenResponse.All generateTokenBySignIn(Long accountId) {
    RefreshToken refTokenEntity = refreshTokenRepository.save(RefreshToken.create(accountId));
    return generateAllToken(accountId, refTokenEntity.getId(), refTokenEntity.getCurrentTokenId());
  }

  @Transactional
  public Optional<TokenResponse.All> renewToken(String refreshToken) {
    JwtPayload payload = jwtProvider.decode(refreshToken).orElseThrow(AuthenticationException::new);
    RefreshToken refTokenEntity = refreshTokenRepository.findByIdOrThrow(payload.id());

    try {
      refTokenEntity.renew(payload.subject());
      refreshTokenRepository.save(refTokenEntity);
      return Optional.of(generateAllToken(
          refTokenEntity.getAccountId(),
          refTokenEntity.getId(),
          refTokenEntity.getCurrentTokenId()
      ));
    } catch (BusinessException exception) {
      LOGGER.warn("토큰 탈취가 의심되어 로그아웃을 진행합니다. accountId: {}", refTokenEntity.getAccountId());
      refreshTokenRepository.delete(refTokenEntity);
      return Optional.empty();
    } catch (OptimisticLockException | ObjectOptimisticLockingFailureException exception) {
      return Optional.empty();
    }
  }

  private TokenResponse.All generateAllToken(
      Long accountId,
      Long rootTokenId,
      Long currentTokenId
  ) {
    String subject = currentTokenId.toString();
    String accessToken = generateToken(new JwtPayload(accountId, subject), Duration.ofHours(12));
    String refreshToken = generateToken(new JwtPayload(rootTokenId, subject), Duration.ofDays(14));
    return new TokenResponse.All(accessToken, refreshToken);
  }

}
