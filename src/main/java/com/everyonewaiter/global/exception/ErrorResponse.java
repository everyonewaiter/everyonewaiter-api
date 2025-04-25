package com.everyonewaiter.global.exception;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  private final ErrorCode code;
  private final String message;
  private final Instant timestamp;

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(errorCode, errorCode.getMessage(), Instant.now());
  }

  public static ErrorResponse withMessage(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode, message, Instant.now());
  }

}
