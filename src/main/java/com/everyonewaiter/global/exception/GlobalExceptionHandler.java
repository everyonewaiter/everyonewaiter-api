package com.everyonewaiter.global.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<ErrorResponse> handleIllegal(
      HttpServletRequest request,
      Exception exception
  ) {
    ErrorCode errorCode = ErrorCode.CLIENT_BAD_REQUEST;
    String message = Objects.requireNonNullElse(exception.getMessage(), errorCode.getMessage());
    ExceptionLogger.warn(request, errorCode, message, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleMethodArgsTypeMismatch(
      HttpServletRequest request,
      MethodArgumentTypeMismatchException exception
  ) {
    ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
      HttpServletRequest request,
      MissingServletRequestParameterException exception
  ) {
    ErrorCode errorCode = ErrorCode.MISSING_PARAMETERS;
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpServletRequest request,
      HttpMessageNotReadableException exception
  ) {
    ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
      HttpServletRequest request,
      HttpMediaTypeNotSupportedException exception
  ) {
    ErrorCode errorCode = ErrorCode.INVALID_CONTENT_TYPE;
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      HttpServletRequest request,
      MethodArgumentNotValidException exception
  ) {
    ErrorCode errorCode = ErrorCode.CLIENT_BAD_REQUEST;
    String fieldErrorMessage = exception.getBindingResult()
        .getFieldErrors()
        .getFirst()
        .getDefaultMessage();
    String message = Objects.requireNonNullElse(fieldErrorMessage, errorCode.getMessage());
    ExceptionLogger.warn(request, errorCode, message, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleAuthentication(
      HttpServletRequest request,
      AuthenticationException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();
    ExceptionLogger.info(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleAccessDenied(
      HttpServletRequest request,
      AccessDeniedException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();
    ExceptionLogger.info(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBusiness(
      HttpServletRequest request,
      BusinessException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleFeign(
      HttpServletRequest request,
      FeignException exception
  ) {
    ErrorCode errorCode = ErrorCode.FAILED_EXTERNAL_SERVER_COMMUNICATION;
    String message = exception.contentUTF8();
    ExceptionLogger.warn(request, errorCode, message, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      HttpServletRequest request,
      NoResourceFoundException exception
  ) {
    ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
    ExceptionLogger.warn(request, errorCode, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
      HttpServletRequest request,
      HttpRequestMethodNotSupportedException exception
  ) {
    ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
    String message = errorCode.getMessage()
        .formatted(request.getRequestURI(), exception.getMethod());
    ExceptionLogger.warn(request, errorCode, message, exception);
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleServer(
      HttpServletRequest request,
      Exception exception
  ) {
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    String message = Objects.requireNonNullElse(exception.getMessage(), errorCode.getMessage());
    if (!message.equalsIgnoreCase("Broken pipe")) {
      ExceptionLogger.error(request, errorCode, exception);
    }
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.from(errorCode));
  }

}
