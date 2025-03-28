package com.everyonewaiter.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class ContentCachingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.contentType == MediaType.APPLICATION_JSON_VALUE) {
            filterChain.doFilter(ContentCachingRequestWrapper(request), ContentCachingResponseWrapper(response))
        } else {
            filterChain.doFilter(request, response)
        }
    }
}
