package com.everyonewaiter.global.filter

import com.everyonewaiter.global.extension.headers
import com.everyonewaiter.global.extension.parameters
import com.everyonewaiter.global.extension.requestUri
import com.everyonewaiter.global.extension.xRequestId
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class MDCLoggingFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        MDC.put("requestId", request.xRequestId)
        MDC.put("requestUri", request.requestUri)
        MDC.put("requestParameters", request.parameters)
        MDC.put("requestHeaders", request.headers)
        MDC.put("requestCookies", objectMapper.writeValueAsString(request.cookies))
        filterChain.doFilter(request, response)
    }
}
