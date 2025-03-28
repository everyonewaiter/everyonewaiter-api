package com.everyonewaiter.global.exception

class AccessDeniedException(
    val errorCode: ErrorCode,
    override val message: String,
) : RuntimeException(message)
