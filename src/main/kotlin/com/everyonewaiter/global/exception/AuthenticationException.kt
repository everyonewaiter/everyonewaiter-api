package com.everyonewaiter.global.exception

class AuthenticationException(
    val errorCode: ErrorCode,
    override val message: String,
) : RuntimeException(message)
