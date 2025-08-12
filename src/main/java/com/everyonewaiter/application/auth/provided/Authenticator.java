package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.application.auth.dto.SendAuthCodeRequest;
import com.everyonewaiter.application.auth.dto.SendAuthMailRequest;
import com.everyonewaiter.application.auth.dto.VerifyAuthCodeRequest;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;

public interface Authenticator {

  void checkAuthSuccess(AuthPurpose authPurpose, PhoneNumber phoneNumber);

  void sendAuthCode(AuthPurpose authPurpose, @Valid SendAuthCodeRequest sendAuthCodeRequest);

  void verifyAuthCode(AuthPurpose authPurpose, @Valid VerifyAuthCodeRequest verifyAuthCodeRequest);

  void sendAuthMail(@Valid SendAuthMailRequest sendAuthMailRequest);

  Email verifyAuthMail(String authMailToken);

}
