package com.everyonewaiter.global.exception

class AuthenticationException(
    val errorCode: ErrorCode,
) : RuntimeException()
