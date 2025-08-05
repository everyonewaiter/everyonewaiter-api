package com.everyonewaiter.global.logging;

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
    MDC.put("requestId", HttpRequestParser.parseXRequestId(request));
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestURI", HttpRequestParser.parseRequestURI(request));
    MDC.put("requestParameters", HttpRequestParser.parseParameters(request));
    MDC.put("requestHeaders", HttpRequestParser.parseHeaders(request));
    MDC.put("requestCookies", objectMapper.writeValueAsString(request.getCookies()));

    filterChain.doFilter(request, response);
  }

}
