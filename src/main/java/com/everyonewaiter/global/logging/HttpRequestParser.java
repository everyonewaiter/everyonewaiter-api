package com.everyonewaiter.global.logging;

import static lombok.AccessLevel.PRIVATE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = PRIVATE)
public final class HttpRequestParser {

  private static final String NULL = "NULL";
  private static final String BLIND = "BLIND";
  private static final String DELIMITER = ": ";

  public static String parseXRequestId(HttpServletRequest request) {
    return Objects.requireNonNullElse(request.getHeader("x-request-id"), request.getRequestId());
  }

  public static String parseRequestURI(HttpServletRequest request) {
    String requestURI = request.getRequestURI();

    if (StringUtils.hasText(request.getQueryString())) {
      return requestURI + "?" + request.getQueryString();
    }

    return requestURI;
  }

  public static String parseParameters(HttpServletRequest request) {
    StringBuilder stringBuilder = new StringBuilder();

    request.getParameterNames()
        .asIterator()
        .forEachRemaining(parameterName -> {
          stringBuilder.append(parameterName);
          stringBuilder.append(DELIMITER);
          stringBuilder.append(request.getParameter(parameterName));
          stringBuilder.append(System.lineSeparator());
        });
    String parameters = stringBuilder.toString();

    return StringUtils.hasText(parameters) ? parameters : NULL;
  }

  public static String parseHeaders(HttpServletRequest request) {
    StringBuilder stringBuilder = new StringBuilder();

    request.getHeaderNames()
        .asIterator()
        .forEachRemaining(headerName -> {
          String headerValue = request.getHeader(headerName);
          if (headerName.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)) {
            headerValue = BLIND;
          }

          stringBuilder.append(headerName);
          stringBuilder.append(DELIMITER);
          stringBuilder.append(headerValue);
          stringBuilder.append(System.lineSeparator());
        });
    String headers = stringBuilder.toString();

    return StringUtils.hasText(headers) ? headers : NULL;
  }

}
