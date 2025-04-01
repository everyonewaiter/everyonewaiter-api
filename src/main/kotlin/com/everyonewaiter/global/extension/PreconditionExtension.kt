package com.everyonewaiter.global.extension

import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun checkOrThrow(
    value: Boolean,
    errorCode: ErrorCode,
) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw BusinessException(errorCode)
    }
}
