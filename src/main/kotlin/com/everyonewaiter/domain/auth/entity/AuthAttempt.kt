package com.everyonewaiter.domain.auth.entity

import java.time.Duration

data class AuthAttempt(
    val phoneNumber: String,
    val purpose: AuthPurpose,
    val count: Int = 0,
    val expiration: Duration = Duration.ofDays(1),
) {
    val key: String
        get() = "auth:attempt:${purpose.name.lowercase()}:$phoneNumber"

    val value: String
        get() = count.toString()
}
