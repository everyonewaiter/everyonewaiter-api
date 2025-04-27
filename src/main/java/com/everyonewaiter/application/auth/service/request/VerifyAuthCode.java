package com.everyonewaiter.application.auth.service.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;

public record VerifyAuthCode(String phoneNumber, int code, AuthPurpose purpose) {

}
