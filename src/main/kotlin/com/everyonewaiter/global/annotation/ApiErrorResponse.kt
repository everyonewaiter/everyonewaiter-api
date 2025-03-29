package com.everyonewaiter.global.annotation

import com.everyonewaiter.global.exception.ErrorCode
import org.springframework.http.MediaType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorResponse(
    val code: ErrorCode,
    val summary: String = "",
    val exampleName: String = "",
    val exampleMessage: String = "",
    val mediaType: String = MediaType.APPLICATION_JSON_VALUE,
)
