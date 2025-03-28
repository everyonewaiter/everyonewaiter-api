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
        val message = exception.message ?: "요청을 처리하던 중 잘못된 요청 정보를 발견했어요. 요청 정보를 확인해 주세요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleMethodArgsTypeMismatchException(
        request: HttpServletRequest,
        exception: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_PARAMETER
        val message = "요청을 처리하던 중 잘못된 타입의 파라미터를 발견했어요. 요청 파라미터의 타입을 확인해 주세요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleMissingServletRequestParameterException(
        request: HttpServletRequest,
        exception: MissingServletRequestParameterException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.MISSING_PARAMETERS
        val message = "요청을 처리하던 중 필수 파라미터가 누락된 것을 확인했어요. 누락된 파라미터가 있는지 확인해 주세요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(
        request: HttpServletRequest,
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_REQUEST_BODY
        val message = "요청 본문 읽기에 실패했어요. 요청 본문이 잘못된 형식이거나 누락된 값이 있을 수 있어요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleHttpMediaTypeNotSupportedException(
        request: HttpServletRequest,
        exception: HttpMediaTypeNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_CONTENT_TYPE
        val message = "요청하신 경로에서 지원하지 않는 Content-Type을 사용했어요. 요청 헤더의 Content-Type을 확인해 주세요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleBusinessException(
        request: HttpServletRequest,
        exception: BusinessException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = exception.errorCode
        logWarn(request, errorCode, exception.message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, exception.message))
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
        val message = "페이지를 찾지 못했어요. 주소를 잘못 입력하셨거나 요청하신 페이지가 변경 또는 삭제된 것 같아요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleMethodNotAllowedException(
        request: HttpServletRequest,
        exception: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.METHOD_NOT_ALLOWED
        val message = "요청하신 경로 '${request.requestURI}'는 '${exception.method}' 메서드를 지원하지 않아요."
        logWarn(request, errorCode, message, exception)
        return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode, message))
    }

    @ExceptionHandler
    fun handleInternalServerException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        if (exception.message != "Broken pipe") {
            logError(request, exception)
        }
        return ResponseEntity
            .internalServerError()
            .body(
                ErrorResponse(
                    code = ErrorCode.INTERNAL_SERVER_ERROR,
                    message = "서버에 오류가 발생했어요. 문제 해결을 위해 관리자에게 문의를 남겨주세요.",
                ),
            )
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
