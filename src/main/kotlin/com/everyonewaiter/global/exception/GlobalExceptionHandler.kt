package com.everyonewaiter.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
internal class GlobalExceptionHandler {
    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    fun handleIllegalException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.BAD_REQUEST
        val message = exception.message ?: errorCode.message
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleMethodArgsTypeMismatchException(
        request: HttpServletRequest,
        exception: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_PARAMETER
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleMissingServletRequestParameterException(
        request: HttpServletRequest,
        exception: MissingServletRequestParameterException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.MISSING_PARAMETERS
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(
        request: HttpServletRequest,
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_REQUEST_BODY
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleHttpMediaTypeNotSupportedException(
        request: HttpServletRequest,
        exception: HttpMediaTypeNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_CONTENT_TYPE
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleBusinessException(
        request: HttpServletRequest,
        exception: BusinessException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = exception.errorCode
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleAuthenticationException(
        request: HttpServletRequest,
        exception: AuthenticationException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = exception.errorCode
        if (errorCode == ErrorCode.UNAUTHORIZED) {
            logInfo(request, errorCode, exception.message, exception)
        } else {
            logWarn(request, errorCode, exception.message, exception)
        }
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, exception.message))
    }

    @ExceptionHandler
    fun handleAccessDeniedException(
        request: HttpServletRequest,
        exception: AccessDeniedException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = exception.errorCode
        if (errorCode == ErrorCode.FORBIDDEN) {
            logInfo(request, errorCode, exception.message, exception)
        } else {
            logWarn(request, errorCode, exception.message, exception)
        }
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, exception.message))
    }

    @ExceptionHandler
    fun handleResourceNotFoundException(
        request: HttpServletRequest,
        exception: NoResourceFoundException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.RESOURCE_NOT_FOUND
        logWarn(request, errorCode, errorCode.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, errorCode.message))
    }

    @ExceptionHandler
    fun handleMethodNotAllowedException(
        request: HttpServletRequest,
        exception: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.METHOD_NOT_ALLOWED
        val message = errorCode.message.format(request.requestURI, exception.method)
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleInternalServerException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        if (exception.message != "Broken pipe") {
            logError(request, exception)
        }
        return ResponseEntity.internalServerError().body(ErrorResponse(code = errorCode, message = errorCode.message))
    }

    private fun logInfo(
        request: HttpServletRequest,
        errorCode: ErrorCode,
        message: String,
        exception: Exception,
    ) {
        val method = request.method
        val uri = request.requestURI
        val exceptionName = exception.javaClass.simpleName
        logger.info { "[${errorCode.name}] [$method $uri] [$exceptionName ${errorCode.status}]: $message" }
    }

    private fun logWarn(
        request: HttpServletRequest,
        errorCode: ErrorCode,
        message: String,
        exception: Exception,
    ) {
        val method = request.method
        val uri = request.requestURI
        val exceptionName = exception.javaClass.simpleName
        logger.warn { "[${errorCode.name}] [$method $uri] [$exceptionName ${errorCode.status}]: $message" }
    }

    private fun logError(
        request: HttpServletRequest,
        exception: Exception,
    ) {
        val method = request.method
        val uri = request.requestURI
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val exceptionName = exception.javaClass.simpleName
        logger.error(exception) { "[$method $uri] [$exceptionName $status]: ${exception.message}" }
    }
}
