package com.everyonewaiter.global.exception

class BusinessException(
    val errorCode: ErrorCode,
) : RuntimeException()
