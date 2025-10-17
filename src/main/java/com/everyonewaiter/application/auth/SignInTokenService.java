package com.everyonewaiter.application.auth;


import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.auth.provided.SignInTokenProvider;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.application.auth.required.RefreshTokenRepository;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.RefreshToken;
import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import com.everyonewaiter.domain.shared.AuthenticationException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class SignInTokenService implements SignInTokenProvider {

  private static final Logger LOGGER = getLogger(SignInTokenService.class);

  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public SignInToken createToken(Long accountId) {
    RefreshToken refreshToken = RefreshToken.create(accountId);

    refreshTokenRepository.save(refreshToken);

    return createTokenResponse(refreshToken);
  }

  @Override
  @DistributedLock(key = "#signInTokenRenewRequest.refreshToken")
  public Optional<SignInToken> renewToken(SignInTokenRenewRequest signInTokenRenewRequest) {
    JwtPayload payload = jwtProvider.decode(signInTokenRenewRequest.refreshToken())
        .orElseThrow(AuthenticationException::new);

    RefreshToken refreshToken = refreshTokenRepository.findByIdOrThrow(payload.id());

    try {
      refreshToken.renew(payload.parseLongSubject());

      refreshTokenRepository.save(refreshToken);

      return Optional.of(createTokenResponse(refreshToken));
    } catch (AuthenticationException | NumberFormatException exception) {
      LOGGER.warn("토큰 탈취가 의심되어 로그아웃을 진행합니다. accountId: {}", refreshToken.getAccountId());

      refreshTokenRepository.delete(refreshToken);

      return Optional.empty();
    }
  }

  private SignInToken createTokenResponse(RefreshToken refreshToken) {
    JwtPayload acc = new JwtPayload(refreshToken.getAccountId(), refreshToken.getCurrentTokenId());
    JwtPayload ref = new JwtPayload(refreshToken.getId(), refreshToken.getCurrentTokenId());

    String accessToken = jwtProvider.encode(acc, Duration.ofHours(12));
    String refToken = jwtProvider.encode(ref, Duration.ofDays(14));

    return new SignInToken(accessToken, refToken);
  }

}
