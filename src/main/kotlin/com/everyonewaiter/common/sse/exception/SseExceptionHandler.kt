package com.everyonewaiter.common.sse.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.async.AsyncRequestTimeoutException

private val logger = KotlinLogging.logger {}

@Order(0)
@RestControllerAdvice
class SseExceptionHandler {
    @ExceptionHandler(value = [AsyncRequestTimeoutException::class], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun handleAsyncRequestTimeout(): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.REQUEST_TIMEOUT)
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body("data: CLOSED\n\n")

    @ExceptionHandler(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun handleInternalServerError(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<String> {
        val message = exception.message ?: "서버 내부에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
        if (exception.message != "Broken pipe") {
            logger.error(exception) { "[SSE] ${exception.message}" }
        }
        return ResponseEntity
            .internalServerError()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body("data: $message\n\n")
    }
}
