package com.everyonewaiter.domain.auth.entity

import java.time.Duration

private const val MIN = 100_000
private const val MAX = 999_999

data class AuthCode(
    val phoneNumber: String,
    val code: Int = (MIN..MAX).random(),
    val expiration: Duration = Duration.ofMinutes(5),
) {
    val key: String
        get() = "auth:code:$phoneNumber"

    val value: String
        get() = code.toString()
}
