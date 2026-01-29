package com.everyonewaiter.adapter.web.utils;

import static com.everyonewaiter.adapter.web.utils.HttpRequestParser.parseCookies;
import static com.everyonewaiter.adapter.web.utils.HttpRequestParser.parseHeaders;
import static com.everyonewaiter.adapter.web.utils.HttpRequestParser.parseParameters;
import static com.everyonewaiter.adapter.web.utils.HttpRequestParser.parseRequestUri;
import static com.everyonewaiter.adapter.web.utils.HttpRequestParser.parseXRequestId;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order
@Component
class MDCLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    MDC.put("requestId", parseXRequestId(request));
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestUri", parseRequestUri(request));
    MDC.put("requestParameters", parseParameters(request));
    MDC.put("requestHeaders", parseHeaders(request));
    MDC.put("requestCookies", parseCookies(request));

    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }

}
