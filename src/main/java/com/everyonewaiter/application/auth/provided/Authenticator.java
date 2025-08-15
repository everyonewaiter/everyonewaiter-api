package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.SendAuthMailRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;

public interface Authenticator {

  void checkAuthSuccess(AuthPurpose authPurpose, PhoneNumber phoneNumber);

  void sendAuthCode(AuthPurpose authPurpose, @Valid SendAuthCodeRequest sendAuthCodeRequest);

  PhoneNumber verifyAuthCode(
      AuthPurpose authPurpose,
      @Valid VerifyAuthCodeRequest verifyAuthCodeRequest
  );

  void sendAuthMail(@Valid SendAuthMailRequest sendAuthMailRequest);

  Email verifyAuthMail(String authMailToken);

}
