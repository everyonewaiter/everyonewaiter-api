package com.everyonewaiter.application.auth.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;

public record SendAuthCode(String phoneNumber, AuthPurpose purpose) {

}
