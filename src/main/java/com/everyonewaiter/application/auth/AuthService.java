package com.everyonewaiter.application.auth;

import com.everyonewaiter.application.account.provided.AccountValidator;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.auth.required.AuthRepository;
import com.everyonewaiter.application.auth.required.JwtProvider;
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
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.SendAuthMailRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class AuthService implements Authenticator {

  private final JwtProvider jwtProvider;
  private final AccountValidator accountValidator;
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
    checkPossibleSendAuthCode(authPurpose, phoneNumber);

    AuthAttempt authAttempt = new AuthAttempt(authPurpose, phoneNumber);
    if (authAttempt.isExceed(authRepository.find(authAttempt))) {
      throw new ExceedMaximumVerificationException();
    }

    AuthCode authCode = new AuthCode(phoneNumber);
    authRepository.save(authCode);
    authRepository.increment(authAttempt);
    authRepository.delete(new AuthSuccess(authPurpose, phoneNumber));

    AuthCodeSendEvent sendEvent = new AuthCodeSendEvent(authCode);
    applicationEventPublisher.publishEvent(sendEvent);
  }

  private void checkPossibleSendAuthCode(AuthPurpose authPurpose, PhoneNumber phoneNumber) {
    if (authPurpose == AuthPurpose.SIGN_UP) {
      accountValidator.checkDuplicatePhone(phoneNumber);
    } else {
      accountValidator.checkPossibleSendAuthCode(phoneNumber);
    }
  }

  @Override
  public PhoneNumber verifyAuthCode(
      AuthPurpose authPurpose,
      VerifyAuthCodeRequest verifyAuthCodeRequest
  ) {
    PhoneNumber phoneNumber = new PhoneNumber(verifyAuthCodeRequest.phoneNumber());

    AuthSuccess authSuccess = new AuthSuccess(authPurpose, phoneNumber);
    if (authRepository.exists(authSuccess)) {
      throw new AlreadyVerifiedPhoneException();
    }

    AuthCode authCode = new AuthCode(phoneNumber, verifyAuthCodeRequest.code());
    authCode.verify(authRepository.find(authCode));

    authRepository.save(authSuccess);
    authRepository.delete(authCode);

    return phoneNumber;
  }

  @Override
  public void sendAuthMail(SendAuthMailRequest sendAuthMailRequest) {
    Email email = new Email(sendAuthMailRequest.email());

    accountValidator.checkPossibleSendAuthMail(email);

    applicationEventPublisher.publishEvent(new AuthMailSendEvent(email));
  }

  @Override
  public Email verifyAuthMail(String authMailToken) {
    JwtPayload payload = jwtProvider.decode(authMailToken)
        .orElseThrow(ExpiredVerificationEmailException::new);

    if (!JwtFixedId.VERIFICATION_EMAIL_ID.equals(payload.id())) {
      throw new ExpiredVerificationEmailException();
    }

    return new Email(payload.subject());
  }

}
