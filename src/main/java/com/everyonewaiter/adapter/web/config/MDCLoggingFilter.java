package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.adapter.web.HttpRequestParser.parseHeaders;
import static com.everyonewaiter.adapter.web.HttpRequestParser.parseParameters;
import static com.everyonewaiter.adapter.web.HttpRequestParser.parseRequestUri;
import static com.everyonewaiter.adapter.web.HttpRequestParser.parseXRequestId;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
class MDCLoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    MDC.put("requestId", parseXRequestId(request));
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestUri", parseRequestUri(request));
    MDC.put("requestParameters", parseParameters(request));
    MDC.put("requestHeaders", parseHeaders(request));
    MDC.put("requestCookies", objectMapper.writeValueAsString(request.getCookies()));

    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }

}
