package com.everyonewaiter.application.auth;

import com.everyonewaiter.application.auth.dto.SendAuthCodeRequest;
import com.everyonewaiter.application.auth.dto.SendAuthMailRequest;
import com.everyonewaiter.application.auth.dto.VerifyAuthCodeRequest;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.auth.required.AuthRepository;
import com.everyonewaiter.application.auth.required.JwtDecoder;
import com.everyonewaiter.domain.auth.AlreadyVerifiedPhoneException;
import com.everyonewaiter.domain.auth.AuthAttempt;
import com.everyonewaiter.domain.auth.AuthCode;
import com.everyonewaiter.domain.auth.AuthCodeSendEvent;
import com.everyonewaiter.domain.auth.AuthMailSendEvent;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.AuthSuccess;
import com.everyonewaiter.domain.auth.ExceedMaximumVerificationException;
import com.everyonewaiter.domain.auth.ExpiredVerificationEmailException;
import com.everyonewaiter.domain.auth.ExpiredVerificationPhoneException;
import com.everyonewaiter.domain.auth.JwtFixedId;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class AuthService implements Authenticator {

  private final JwtDecoder jwtDecoder;
  private final AuthRepository authRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void checkAuthSuccess(AuthPurpose authPurpose, PhoneNumber phoneNumber) {
    AuthSuccess authSuccess = new AuthSuccess(authPurpose, phoneNumber);

    if (!authRepository.exists(authSuccess)) {
      throw new ExpiredVerificationPhoneException();
    }
  }

  @Override
  public void sendAuthCode(AuthPurpose authPurpose, SendAuthCodeRequest sendAuthCodeRequest) {
    PhoneNumber phoneNumber = new PhoneNumber(sendAuthCodeRequest.phoneNumber());
    // 휴대폰 검증 - AccountValidator

    AuthAttempt authAttempt = new AuthAttempt(authPurpose, phoneNumber);
    if (authAttempt.isExceed(authRepository.find(authAttempt))) {
      throw new ExceedMaximumVerificationException();
    }

    AuthCode authCode = new AuthCode(phoneNumber);
    authRepository.save(authCode);
    authRepository.increment(authAttempt);
    authRepository.delete(new AuthSuccess(authPurpose, phoneNumber));

    AuthCodeSendEvent sendEvent = new AuthCodeSendEvent(authCode.phoneNumber(), authCode.code());
    applicationEventPublisher.publishEvent(sendEvent);
  }

  @Override
  public void verifyAuthCode(AuthPurpose authPurpose, VerifyAuthCodeRequest verifyAuthCodeRequest) {
    PhoneNumber phoneNumber = new PhoneNumber(verifyAuthCodeRequest.phoneNumber());

    AuthSuccess authSuccess = new AuthSuccess(authPurpose, phoneNumber);
    if (authRepository.exists(authSuccess)) {
      throw new AlreadyVerifiedPhoneException();
    }

    AuthCode authCode = new AuthCode(phoneNumber, verifyAuthCodeRequest.code());
    authCode.verify(authRepository.find(authCode));

    authRepository.save(authSuccess);
    authRepository.delete(authCode);
  }

  @Override
  public void sendAuthMail(SendAuthMailRequest sendAuthMailRequest) {
    Email email = new Email(sendAuthMailRequest.email());
    // 이메일 검증 - AccountValidator

    applicationEventPublisher.publishEvent(new AuthMailSendEvent(email));
  }

  @Override
  public Email verifyAuthMail(String authMailToken) {
    JwtPayload payload = jwtDecoder.decode(authMailToken)
        .orElseThrow(ExpiredVerificationEmailException::new);

    if (!JwtFixedId.VERIFICATION_EMAIL_ID.equals(payload.id())) {
      throw new ExpiredVerificationEmailException();
    }

    return new Email(payload.subject());
  }

}
