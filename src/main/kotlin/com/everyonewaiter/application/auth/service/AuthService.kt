package com.everyonewaiter.application.auth.service

import com.everyonewaiter.domain.auth.entity.AuthAttempt
import com.everyonewaiter.domain.auth.entity.AuthCode
import com.everyonewaiter.domain.auth.entity.AuthPurpose
import com.everyonewaiter.domain.auth.entity.AuthSuccess
import com.everyonewaiter.domain.auth.event.AuthCodeCreateEvent
import com.everyonewaiter.domain.auth.repository.AuthAttemptRepository
import com.everyonewaiter.domain.auth.repository.AuthCodeRepository
import com.everyonewaiter.domain.auth.repository.AuthSuccessRepository
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authAttemptRepository: AuthAttemptRepository,
    private val authCodeRepository: AuthCodeRepository,
    private val authSuccessRepository: AuthSuccessRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun checkAuthSuccessExists(phoneNumber: String) {
        val authSuccess = AuthSuccess(phoneNumber)
        checkOrThrow(authSuccessRepository.exists(authSuccess), ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER)
    }

    fun checkAuthAttemptExceed(
        phoneNumber: String,
        purpose: AuthPurpose,
    ) {
        val authAttempt = AuthAttempt(phoneNumber, purpose)
        val isExceed = authAttemptRepository.find(authAttempt) < purpose.maxAttempt
        checkOrThrow(isExceed, ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER)
    }

    fun generateCode(
        phoneNumber: String,
        purpose: AuthPurpose,
    ) {
        val authCode = AuthCode(phoneNumber)
        authCodeRepository.save(authCode)
        authAttemptRepository.increment(AuthAttempt(phoneNumber, purpose))
        applicationEventPublisher.publishEvent(AuthCodeCreateEvent(phoneNumber, authCode.code))
    }
}
