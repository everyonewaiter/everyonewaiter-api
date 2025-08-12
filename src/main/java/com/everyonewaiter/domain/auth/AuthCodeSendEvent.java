package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.PhoneNumber;

public record AuthCodeSendEvent(PhoneNumber phoneNumber, int code) {

}
