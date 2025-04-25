package com.everyonewaiter.global.extension;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestParser {

  public static String getXRequestId(HttpServletRequest request) {
    return Objects.requireNonNullElse(request.getHeader("x-request-id"), request.getRequestId());
  }

}
