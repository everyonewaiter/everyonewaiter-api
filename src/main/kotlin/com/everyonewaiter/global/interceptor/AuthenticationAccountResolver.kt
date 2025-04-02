package com.everyonewaiter.global.interceptor

import com.everyonewaiter.common.jwt.JwtProvider
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.entity.AccountPermission
import com.everyonewaiter.domain.account.repository.AccountRepository
import com.everyonewaiter.global.annotation.AuthenticationAccount
import com.everyonewaiter.global.exception.AuthenticationException
import com.everyonewaiter.global.exception.AuthorizationException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val BEARER = "Bearer "

@Component
class AuthenticationAccountResolver(
    private val jwtProvider: JwtProvider,
    private val accountRepository: AccountRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAuthenticationAccountAnnotation = parameter.hasParameterAnnotation(AuthenticationAccount::class.java)
        val isCorrectParameterType = Account::class.java.isAssignableFrom(parameter.parameterType)
        return hasAuthenticationAccountAnnotation && isCorrectParameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Account {
        val accessToken = extractAccessToken(webRequest)
        val payload = jwtProvider
            .decode(accessToken)
            .getOrNull() ?: throw AuthenticationException(ErrorCode.UNAUTHORIZED)
        val account = accountRepository.findById(payload.id) ?: throw AuthenticationException(ErrorCode.UNAUTHORIZED)
        checkOrThrow(account.isActive, ErrorCode.UNAUTHORIZED)
        validatePermission(account.permission, parameter)
        return account
    }

    private fun extractAccessToken(request: NativeWebRequest): String {
        val authorization =
            request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw AuthenticationException(ErrorCode.UNAUTHORIZED)
        if (authorization.length > BEARER.length && authorization.startsWith(BEARER)) {
            return authorization.substring(BEARER.length)
        }
        throw AuthenticationException(ErrorCode.UNAUTHORIZED)
    }

    @Suppress("kotlin:S6619")
    private fun validatePermission(
        permission: AccountPermission,
        parameter: MethodParameter,
    ) {
        val annotation = parameter.getParameterAnnotation(AuthenticationAccount::class.java)
            ?: throw AuthenticationException(ErrorCode.UNAUTHORIZED)
        if (annotation.permission == AccountPermission.ADMIN && permission != AccountPermission.ADMIN) {
            throw AuthorizationException(ErrorCode.FORBIDDEN)
        }
        if (annotation.permission == AccountPermission.OWNER && permission == AccountPermission.USER) {
            throw AuthorizationException(ErrorCode.FORBIDDEN)
        }
    }
}
