package com.everyonewaiter.global.exception

class BusinessException(
    val errorCode: ErrorCode,
    override val message: String,
) : RuntimeException(message)
