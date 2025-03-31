package com.everyonewaiter.domain.auth.entity

enum class AuthPurpose(
    val maxAttempt: Int,
) {
    SIGN_UP(5),
    CREATE_DEVICE(50),
}
