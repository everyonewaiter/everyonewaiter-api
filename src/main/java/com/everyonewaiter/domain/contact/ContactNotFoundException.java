package com.everyonewaiter.domain.contact;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ContactNotFoundException extends BusinessException {

  public ContactNotFoundException() {
    super(ErrorCode.CONTACT_NOT_FOUND);
  }

}
