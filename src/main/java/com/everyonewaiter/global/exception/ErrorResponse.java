package com.everyonewaiter.global.exception;

import java.time.Instant;

public record ErrorResponse(ErrorCode code, String message, Instant timestamp) {

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(errorCode, errorCode.getMessage(), Instant.now());
  }

  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode, message, Instant.now());
  }

}
