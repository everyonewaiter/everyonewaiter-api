package com.everyonewaiter.application.auth.service

import com.everyonewaiter.application.auth.dto.VerifyAuthCode
import com.everyonewaiter.common.jwt.JwtPayload
import com.everyonewaiter.common.jwt.JwtProvider
import com.everyonewaiter.domain.auth.entity.AuthAttempt
import com.everyonewaiter.domain.auth.entity.AuthCode
import com.everyonewaiter.domain.auth.entity.AuthPurpose
import com.everyonewaiter.domain.auth.entity.AuthSuccess
import com.everyonewaiter.domain.auth.event.AuthCodeCreateEvent
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent
import com.everyonewaiter.domain.auth.repository.AuthAttemptRepository
import com.everyonewaiter.domain.auth.repository.AuthCodeRepository
import com.everyonewaiter.domain.auth.repository.AuthSuccessRepository
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val authAttemptRepository: AuthAttemptRepository,
    private val authCodeRepository: AuthCodeRepository,
    private val authSuccessRepository: AuthSuccessRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun checkAuthSuccessExists(phoneNumber: String) {
        checkOrThrow(existsSuccess(phoneNumber), ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER)
    }

    fun checkAuthSuccessNotExists(phoneNumber: String) {
        checkOrThrow(existsSuccess(phoneNumber).not(), ErrorCode.ALREADY_VERIFIED_PHONE_NUMBER)
    }

    private fun existsSuccess(phoneNumber: String) = authSuccessRepository.exists(AuthSuccess(phoneNumber))

    fun checkAuthAttemptExceed(
        phoneNumber: String,
        purpose: AuthPurpose,
    ) {
        val authAttempt = AuthAttempt(phoneNumber, purpose)
        val isExceed = authAttemptRepository.find(authAttempt) < purpose.maxAttempt
        checkOrThrow(isExceed, ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER)
    }

    fun generateAccessTokenBySignIn(
        accountId: Long,
        email: String,
    ): String = jwtProvider.generate(JwtPayload(accountId.toString(), email))

    fun generateCode(
        phoneNumber: String,
        purpose: AuthPurpose,
    ) {
        val authCode = AuthCode(phoneNumber)
        authCodeRepository.save(authCode)
        authAttemptRepository.increment(AuthAttempt(phoneNumber, purpose))
        applicationEventPublisher.publishEvent(AuthCodeCreateEvent(phoneNumber, authCode.code))
    }

    fun verifyCode(request: VerifyAuthCode.Request) {
        val code = authCodeRepository.find(AuthCode(request.phoneNumber))
        checkOrThrow(code != null, ErrorCode.EXPIRED_VERIFICATION_CODE)
        checkOrThrow(code == request.code, ErrorCode.UNMATCHED_VERIFICATION_CODE)
        authSuccessRepository.save(AuthSuccess(request.phoneNumber))
    }

    fun sendAuthMail(email: String) {
        applicationEventPublisher.publishEvent(AuthMailSendEvent(email))
    }
}
