package com.everyonewaiter.domain.auth.entity

import java.time.Duration

data class AuthSuccess(
    val phoneNumber: String,
    val expiration: Duration = Duration.ofMinutes(15),
) {
    val key: String
        get() = "auth:success:$phoneNumber"

    val value: String
        get() = phoneNumber
}
