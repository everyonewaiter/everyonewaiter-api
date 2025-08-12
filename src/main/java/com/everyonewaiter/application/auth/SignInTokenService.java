package com.everyonewaiter.application.auth;


import com.everyonewaiter.application.auth.dto.TokenResponse;
import com.everyonewaiter.application.auth.provided.SignInTokenProvider;
import com.everyonewaiter.application.auth.required.JwtDecoder;
import com.everyonewaiter.application.auth.required.JwtEncoder;
import com.everyonewaiter.application.auth.required.RefreshTokenRepository;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.RefreshToken;
import com.everyonewaiter.domain.shared.AuthenticationException;
import com.everyonewaiter.domain.shared.BusinessException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class SignInTokenService implements SignInTokenProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(SignInTokenService.class);

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  @Transactional
  public TokenResponse createToken(Long accountId) {
    RefreshToken refreshToken = RefreshToken.create(accountId);

    refreshTokenRepository.save(refreshToken);

    return createTokenResponse(refreshToken);
  }

  @Override
  @Transactional
  @DistributedLock(key = "#refreshToken")
  public Optional<TokenResponse> renewToken(String refToken) {
    JwtPayload payload = jwtDecoder.decode(refToken).orElseThrow(AuthenticationException::new);
    RefreshToken refreshToken = refreshTokenRepository.findByIdOrThrow(payload.id());

    try {
      refreshToken.renew(payload.parseLongSubject());

      refreshTokenRepository.save(refreshToken);

      return Optional.of(createTokenResponse(refreshToken));
    } catch (BusinessException | NumberFormatException exception) {
      LOGGER.warn("토큰 탈취가 의심되어 로그아웃을 진행합니다. accountId: {}", refreshToken.getAccountId());

      refreshTokenRepository.delete(refreshToken);

      return Optional.empty();
    }
  }

  private TokenResponse createTokenResponse(RefreshToken refreshToken) {
    JwtPayload acc = new JwtPayload(refreshToken.getAccountId(), refreshToken.getCurrentTokenId());
    JwtPayload ref = new JwtPayload(refreshToken.getId(), refreshToken.getCurrentTokenId());

    String accessToken = jwtEncoder.encode(acc, Duration.ofHours(12));
    String refToken = jwtEncoder.encode(ref, Duration.ofDays(14));

    return new TokenResponse(accessToken, refToken);
  }

}
