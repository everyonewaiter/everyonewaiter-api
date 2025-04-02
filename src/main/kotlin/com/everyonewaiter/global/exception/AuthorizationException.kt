package com.everyonewaiter.global.exception

class AuthorizationException(
    val errorCode: ErrorCode,
) : RuntimeException()
