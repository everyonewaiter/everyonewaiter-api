package com.everyonewaiter.application.auth.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;

public class AuthWrite {

  public record SendAuthCode(String phoneNumber, AuthPurpose purpose) {

  }

  public record VerifyAuthCode(String phoneNumber, int code, AuthPurpose purpose) {

  }

}
