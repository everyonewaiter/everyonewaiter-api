package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.PhoneNumber;

public record AuthCodeSendEvent(PhoneNumber phoneNumber, int code) {

  public AuthCodeSendEvent(AuthCode authCode) {
    this(authCode.phoneNumber(), authCode.code());
  }

}
