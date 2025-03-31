package com.everyonewaiter.global.extension

import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode

fun checkOrThrow(
    value: Boolean,
    errorCode: ErrorCode,
) {
    if (!value) {
        throw BusinessException(errorCode)
    }
}
