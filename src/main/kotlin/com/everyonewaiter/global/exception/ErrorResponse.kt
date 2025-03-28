package com.everyonewaiter.global.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val code: ErrorCode,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
