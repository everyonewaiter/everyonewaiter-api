package com.everyonewaiter.application.auth.service.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;

public record SendAuthCode(String phoneNumber, AuthPurpose purpose) {

}
