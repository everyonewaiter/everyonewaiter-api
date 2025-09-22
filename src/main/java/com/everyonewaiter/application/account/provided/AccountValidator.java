package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;

public interface AccountValidator {

  void checkDuplicateEmail(Email email);

  void checkDuplicatePhone(PhoneNumber phoneNumber);

  void checkPossibleSendAuthMail(Email email);

  void checkPossibleSendAuthCode(PhoneNumber phoneNumber);

}
