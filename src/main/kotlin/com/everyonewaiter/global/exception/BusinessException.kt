package com.everyonewaiter.global.exception

class BusinessException(
    val errorCode: ErrorCode,
    val resources: Map<String, Any>? = null,
) : RuntimeException()
