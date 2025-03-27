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
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class MDCLoggingFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cachingRequest = request as ContentCachingRequestWrapper
        MDC.put("requestId", cachingRequest.xRequestId)
        MDC.put("requestUri", cachingRequest.requestUri)
        MDC.put("requestParameters", cachingRequest.parameters)
        MDC.put("requestHeaders", cachingRequest.headers)
        MDC.put("requestCookies", objectMapper.writeValueAsString(cachingRequest.cookies))
        filterChain.doFilter(request, response)
    }
}
