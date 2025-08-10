package com.everyonewaiter.global.logging;

import static com.everyonewaiter.global.logging.HttpRequestParser.parseHeaders;
import static com.everyonewaiter.global.logging.HttpRequestParser.parseParameters;
import static com.everyonewaiter.global.logging.HttpRequestParser.parseRequestURI;
import static com.everyonewaiter.global.logging.HttpRequestParser.parseXRequestId;

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
    MDC.put("requestURI", parseRequestURI(request));
    MDC.put("requestParameters", parseParameters(request));
    MDC.put("requestHeaders", parseHeaders(request));
    MDC.put("requestCookies", objectMapper.writeValueAsString(request.getCookies()));

    filterChain.doFilter(request, response);
  }

}
