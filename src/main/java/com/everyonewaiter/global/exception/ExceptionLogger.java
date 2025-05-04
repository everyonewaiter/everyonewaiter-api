package com.everyonewaiter.global.exception;

import com.everyonewaiter.global.support.HttpRequestParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ExceptionLogger {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);
  private static final String DEFAULT_LOGGING_FORMAT = "[{}] [{} {}] [{}] [{} {}]: {}";

  public static void info(HttpServletRequest request, ErrorCode errorCode, Exception exception) {
    String requestId = HttpRequestParser.getXRequestId(request);
    LOGGER.info(DEFAULT_LOGGING_FORMAT, errorCode.name(), request.getMethod(),
        request.getRequestURI(), requestId, exception.getClass().getSimpleName(),
        errorCode.getStatus(), exception.getMessage());
  }

  public static void warn(HttpServletRequest request, ErrorCode errorCode, Exception exception) {
    warn(request, errorCode, exception.getMessage(), exception);
  }

  public static void warn(
      HttpServletRequest request,
      ErrorCode errorCode,
      String message,
      Exception exception
  ) {
    String requestId = HttpRequestParser.getXRequestId(request);
    LOGGER.warn(DEFAULT_LOGGING_FORMAT, errorCode.name(), request.getMethod(),
        request.getRequestURI(), requestId, exception.getClass().getSimpleName(),
        errorCode.getStatus(), message);
  }

  public static void error(HttpServletRequest request, ErrorCode errorCode, Exception exception) {
    String requestId = HttpRequestParser.getXRequestId(request);
    LOGGER.error(DEFAULT_LOGGING_FORMAT, errorCode.name(), request.getMethod(),
        request.getRequestURI(), requestId, exception.getClass().getSimpleName(),
        errorCode.getStatus(), exception.getMessage(), exception);
  }

}
