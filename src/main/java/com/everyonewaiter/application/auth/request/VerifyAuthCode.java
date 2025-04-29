package com.everyonewaiter.application.auth.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;

public record VerifyAuthCode(String phoneNumber, int code, AuthPurpose purpose) {

}
