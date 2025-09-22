package com.everyonewaiter.adapter.web.api;

import static com.everyonewaiter.domain.shared.ErrorCode.CLIENT_BAD_REQUEST;
import static com.everyonewaiter.domain.shared.ErrorCode.FAILED_EXTERNAL_SERVER_COMMUNICATION;
import static com.everyonewaiter.domain.shared.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.everyonewaiter.domain.shared.ErrorCode.INVALID_CONTENT_TYPE;
import static com.everyonewaiter.domain.shared.ErrorCode.INVALID_PARAMETER;
import static com.everyonewaiter.domain.shared.ErrorCode.INVALID_REQUEST_BODY;
import static com.everyonewaiter.domain.shared.ErrorCode.METHOD_NOT_ALLOWED;
import static com.everyonewaiter.domain.shared.ErrorCode.MISSING_PARAMETERS;
import static com.everyonewaiter.domain.shared.ErrorCode.RESOURCE_NOT_FOUND;
import static java.util.Objects.requireNonNullElse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.AuthenticationException;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import feign.FeignException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.DisconnectedClientHelper;

@RestControllerAdvice
class WebApiControllerAdvice {

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<Object> handleIllegal(
      HttpServletRequest request,
      RuntimeException exception
  ) {
    ErrorCode errorCode = CLIENT_BAD_REQUEST;

    String message = requireNonNullElse(exception.getMessage(), errorCode.getMessage());
    WebApiExceptionLogger.warn(request, errorCode, message, exception);

    return createResponseEntity(request, ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleMethodArgsTypeMismatch(
      HttpServletRequest request,
      MethodArgumentTypeMismatchException exception
  ) {
    ErrorCode errorCode = INVALID_PARAMETER;

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler({
      MissingServletRequestParameterException.class,
      MissingServletRequestPartException.class
  })
  public ResponseEntity<Object> handleMissingServletRequest(
      HttpServletRequest request,
      ServletException exception
  ) {
    ErrorCode errorCode = MISSING_PARAMETERS;

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpServletRequest request,
      HttpMessageNotReadableException exception
  ) {
    ErrorCode errorCode = INVALID_REQUEST_BODY;

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpServletRequest request,
      HttpMediaTypeNotSupportedException exception
  ) {
    ErrorCode errorCode = INVALID_CONTENT_TYPE;

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      HttpServletRequest request,
      MethodArgumentNotValidException exception
  ) {
    ErrorCode errorCode = CLIENT_BAD_REQUEST;

    String fieldErrorMessage = exception.getBindingResult()
        .getFieldErrors()
        .getFirst()
        .getDefaultMessage();
    String message = requireNonNullElse(fieldErrorMessage, errorCode.getMessage());
    WebApiExceptionLogger.warn(request, errorCode, message, exception);

    return createResponseEntity(request, ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleAuthentication(
      HttpServletRequest request,
      AuthenticationException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();

    WebApiExceptionLogger.info(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleAccessDenied(
      HttpServletRequest request,
      AccessDeniedException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();

    WebApiExceptionLogger.info(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleBusiness(
      HttpServletRequest request,
      BusinessException exception
  ) {
    ErrorCode errorCode = exception.getErrorCode();

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleFeign(
      HttpServletRequest request,
      FeignException exception
  ) {
    ErrorCode errorCode = FAILED_EXTERNAL_SERVER_COMMUNICATION;

    String message = exception.contentUTF8();
    WebApiExceptionLogger.warn(request, errorCode, message, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleResourceNotFound(
      HttpServletRequest request,
      NoResourceFoundException exception
  ) {
    ErrorCode errorCode = RESOURCE_NOT_FOUND;

    WebApiExceptionLogger.warn(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleMethodNotAllowed(
      HttpServletRequest request,
      HttpRequestMethodNotSupportedException exception
  ) {
    ErrorCode errorCode = METHOD_NOT_ALLOWED;

    String message = errorCode.getMessage()
        .formatted(request.getRequestURI(), exception.getMethod());
    WebApiExceptionLogger.warn(request, errorCode, message, exception);

    return createResponseEntity(request, ErrorResponse.of(errorCode, message));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleAsyncRequestTimeout(
      HttpServletRequest request,
      AsyncRequestTimeoutException exception
  ) {
    ErrorCode errorCode = ErrorCode.REQUEST_TIMEOUT;

    WebApiExceptionLogger.error(request, errorCode, exception);

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler({AsyncRequestNotUsableException.class, ClientAbortException.class})
  public ResponseEntity<Object> handleException(HttpServletRequest request, Exception exception) {
    ErrorCode errorCode = ErrorCode.REQUEST_TIMEOUT;

    if (!DisconnectedClientHelper.isClientDisconnectedException(exception)) {
      WebApiExceptionLogger.error(request, errorCode, exception);
    }

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleServer(
      HttpServletRequest request,
      Exception exception
  ) {
    ErrorCode errorCode = INTERNAL_SERVER_ERROR;

    String message = requireNonNullElse(exception.getMessage(), errorCode.getMessage());
    if (!message.equalsIgnoreCase("Broken pipe")) {
      WebApiExceptionLogger.error(request, errorCode, exception);
    }

    return createResponseEntity(request, ErrorResponse.from(errorCode));
  }

  private ResponseEntity<Object> createResponseEntity(
      HttpServletRequest request,
      ErrorResponse response
  ) {
    if (isSseRequest(request)) {
      return ResponseEntity
          .status(response.code().getStatus())
          .contentType(TEXT_EVENT_STREAM)
          .body("data: " + response.message() + "\n\n");
    } else {
      return ResponseEntity
          .status(response.code().getStatus())
          .contentType(APPLICATION_JSON)
          .body(response);
    }
  }

  private boolean isSseRequest(HttpServletRequest request) {
    return request.getHeader("Accept").equalsIgnoreCase(TEXT_EVENT_STREAM_VALUE);
  }

}
