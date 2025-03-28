package com.everyonewaiter.global.interceptor

import com.everyonewaiter.global.extension.body
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

private val logger = KotlinLogging.logger {}

class LoggingInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        MDC.put("startTime", System.currentTimeMillis().toString())
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        exception: Exception?,
    ) {
        val requestId = MDC.get("requestId")
        val requestUri = MDC.get("requestUri")
        val requestHeaders = MDC.get("requestHeaders")
        val duration = System.currentTimeMillis() - MDC.get("startTime").toLong()
        logger.info { "[REQUEST] [$requestId] [${request.method} $requestUri] [$handler]" }
        logger.info { "[REQUEST HEADERS] ${requestHeaders.replace("\n", ",")}" }
        if (request is ContentCachingRequestWrapper) logger.info { "[REQUEST BODY] ${request.body}" }
        if (response is ContentCachingResponseWrapper) logger.info { "[RESPONSE BODY] ${response.body}" }
        logger.info { "[RESPONSE ${response.status}] [$requestId] [${request.method} $requestUri] [$handler] [${duration}ms]" }
        MDC.clear()
    }
}
