package com.everyonewaiter.global.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorResponses(
    val summary: String = "",
    val value: Array<ApiErrorResponse> = [],
)
