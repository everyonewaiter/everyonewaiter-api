package com.everyonewaiter.domain.contact;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyExistsUncompletedContactException extends BusinessException {

  public AlreadyExistsUncompletedContactException() {
    super(ErrorCode.ALREADY_EXISTS_CONTACT);
  }

}
