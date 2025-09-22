package com.everyonewaiter.domain.auth;

import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuthFixture {

  public static AuthAttempt createAuthAttempt() {
    return new AuthAttempt(AuthPurpose.SIGN_UP, new PhoneNumber("01012345678"));
  }

  public static AuthCode createAuthCode() {
    return new AuthCode(new PhoneNumber("01012345678"), 123456);
  }

  public static AuthSuccess createAuthSuccess() {
    return createAuthSuccess(AuthPurpose.SIGN_UP);
  }

  public static AuthSuccess createAuthSuccess(AuthPurpose authPurpose) {
    return new AuthSuccess(authPurpose, new PhoneNumber("01012345678"));
  }

  public static SendAuthCodeRequest createSendAuthCodeRequest() {
    return new SendAuthCodeRequest("01012345678");
  }

  public static VerifyAuthCodeRequest createVerifyAuthCodeRequest() {
    return new VerifyAuthCodeRequest("01012345678", 123456);
  }

  public static SendAuthMailRequest createSendAuthMailRequest() {
    return new SendAuthMailRequest("admin@everyonewaiter.com");
  }

  public static SignInTokenRenewRequest createSignInTokenRenewRequest(String refreshToken) {
    return new SignInTokenRenewRequest(refreshToken);
  }

}
