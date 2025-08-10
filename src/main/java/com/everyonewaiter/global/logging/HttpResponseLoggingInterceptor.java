package com.everyonewaiter.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class HttpResponseLoggingInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(HttpResponseLoggingInterceptor.class);

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler
  ) throws Exception {
    MDC.put("startTime", String.valueOf(System.currentTimeMillis()));

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler,
      Exception exception
  ) throws Exception {
    long endTime = System.currentTimeMillis() - Long.parseLong(MDC.get("startTime"));

    String requestId = MDC.get("requestId");
    String requestMethod = MDC.get("requestMethod");
    String requestURI = MDC.get("requestURI");

    LOGGER.info("[RESPONSE {}] [{} {}] [{}] [{}] [{} ms]",
        response.getStatus(), requestMethod, requestURI, requestId, handler, endTime);
    MDC.clear();

    HandlerInterceptor.super.afterCompletion(request, response, handler, exception);
  }

}
