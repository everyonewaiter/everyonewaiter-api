package com.everyonewaiter.application.auth.provided;

import static com.everyonewaiter.domain.auth.AuthFixture.createSignInTokenRenewRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.RefreshToken;
import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import com.everyonewaiter.domain.shared.AuthenticationException;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class SignInTokenProviderTest extends IntegrationTest {

  private final EntityManager entityManager;
  private final JwtProvider jwtProvider;
  private final SignInTokenProvider signInTokenProvider;

  @Test
  void createToken() {
    SignInToken signInToken = signInTokenProvider.createToken(1L);

    JwtPayload accPayload = jwtProvider.decode(signInToken.accessToken()).orElseThrow();
    JwtPayload refPayload = jwtProvider.decode(signInToken.refreshToken()).orElseThrow();

    assertThat(accPayload.id()).isEqualTo(1L);
    assertThat(Long.parseLong(accPayload.subject())).isEqualTo(refPayload.id());
    assertThat(Long.parseLong(refPayload.subject())).isEqualTo(refPayload.id());
  }

  @Test
  void renewToken() {
    var token = signInTokenProvider.createToken(1L);

    var request = createSignInTokenRenewRequest(token.refreshToken());

    SignInToken signInToken = signInTokenProvider.renewToken(request).orElseThrow();

    JwtPayload accPayload = jwtProvider.decode(signInToken.accessToken()).orElseThrow();
    JwtPayload refPayload = jwtProvider.decode(signInToken.refreshToken()).orElseThrow();

    assertThat(accPayload.id()).isEqualTo(1L);
    assertThat(accPayload.subject()).isEqualTo(refPayload.subject());
    assertThat(refPayload.id()).isNotEqualTo(Long.parseLong(refPayload.subject()));
  }

  @Test
  void renewTokenFailByDecode() {
    var request = createSignInTokenRenewRequest("invalid token");

    assertThatThrownBy(() -> signInTokenProvider.renewToken(request))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void renewTokenFailBySteal() {
    var token = signInTokenProvider.createToken(1L);

    var request = createSignInTokenRenewRequest(token.refreshToken());

    SignInToken successSignInToken = signInTokenProvider.renewToken(request).orElseThrow();

    // 이미 갱신된 리프레시 토큰으로 갱신 요청 시 토큰이 탈취되었다고 판단
    Optional<SignInToken> failSignInToken = signInTokenProvider.renewToken(request);

    var refreshTokenId = jwtProvider.decode(successSignInToken.refreshToken()).orElseThrow().id();
    var refreshToken = entityManager.find(RefreshToken.class, refreshTokenId);

    assertThat(failSignInToken).isEmpty();
    assertThat(refreshToken).isNull();
  }

  @Test
  void signInTokenRenewRequestFail() {
    checkValidation(new SignInTokenRenewRequest(null));
    checkValidation(new SignInTokenRenewRequest(""));
    checkValidation(new SignInTokenRenewRequest("  "));
  }

  private void checkValidation(SignInTokenRenewRequest signInTokenRenewRequest) {
    assertThatThrownBy(() -> signInTokenProvider.renewToken(signInTokenRenewRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
