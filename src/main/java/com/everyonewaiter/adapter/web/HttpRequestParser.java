package com.everyonewaiter.adapter.web;

import static java.util.Objects.requireNonNullElse;
import static lombok.AccessLevel.PRIVATE;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = PRIVATE)
public final class HttpRequestParser {

  private static final String NULL = "NULL";
  private static final String BLIND = "BLIND";
  private static final String DELIMITER = ": ";

  public static String parseXRequestId(HttpServletRequest request) {
    return requireNonNullElse(request.getHeader("x-request-id"), request.getRequestId());
  }

  public static String parseRequestUri(HttpServletRequest request) {
    String requestUri = request.getRequestURI();

    if (StringUtils.hasText(request.getQueryString())) {
      return requestUri + "?" + request.getQueryString();
    }

    return requestUri;
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
