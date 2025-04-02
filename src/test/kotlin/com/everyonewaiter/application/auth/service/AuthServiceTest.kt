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
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class AuthServiceTest :
    FunSpec({
        val jwtProvider = mockk<JwtProvider>()
        val authAttemptRepository = mockk<AuthAttemptRepository>()
        val authCodeRepository = mockk<AuthCodeRepository>()
        val authSuccessRepository = mockk<AuthSuccessRepository>()
        val applicationEventPublisher = mockk<ApplicationEventPublisher>()
        val authService = AuthService(
            jwtProvider = jwtProvider,
            authAttemptRepository = authAttemptRepository,
            authCodeRepository = authCodeRepository,
            authSuccessRepository = authSuccessRepository,
            applicationEventPublisher = applicationEventPublisher,
        )

        context("checkAuthSuccessExists") {
            val authSuccess = AuthSuccess("01044591812")

            test("인증된 휴대폰이 존재하면 예외가 발생하지 않는다.") {
                every { authSuccessRepository.exists(authSuccess) } returns true
                shouldNotThrowAny { authService.checkAuthSuccessExists(authSuccess.phoneNumber) }
            }

            test("인증된 휴대폰이 존재하지 않으면 예외가 발생한다.") {
                every { authSuccessRepository.exists(authSuccess) } returns false
                shouldThrow<BusinessException> {
                    authService.checkAuthSuccessExists(authSuccess.phoneNumber)
                }.errorCode shouldBe ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER
            }
        }

        context("checkAuthSuccessNotExists") {
            val authSuccess = AuthSuccess("01044591812")

            test("인증된 휴대폰이 존재하지 않으면 예외가 발생하지 않는다.") {
                every { authSuccessRepository.exists(authSuccess) } returns false
                shouldNotThrowAny { authService.checkAuthSuccessNotExists(authSuccess.phoneNumber) }
            }

            test("인증된 휴대폰이 존재하면 예외가 발생한다.") {
                every { authSuccessRepository.exists(authSuccess) } returns true
                shouldThrow<BusinessException> {
                    authService.checkAuthSuccessNotExists(authSuccess.phoneNumber)
                }.errorCode shouldBe ErrorCode.ALREADY_VERIFIED_PHONE_NUMBER
            }
        }

        context("checkAuthAttemptExceed") {
            val authAttempt = AuthAttempt("01044591812", AuthPurpose.SIGN_UP)

            test("인증 시도 횟수가 초과하지 않았다면 예외가 발생하지 않는다.") {
                val copied = authAttempt.copy(purpose = AuthPurpose.CREATE_DEVICE)
                every { authAttemptRepository.find(copied) } returns 10
                shouldNotThrowAny { authService.checkAuthAttemptExceed(copied.phoneNumber, copied.purpose) }
            }

            test("인증 시도 횟수가 초과했다면 예외가 발생한다.") {
                every { authAttemptRepository.find(authAttempt) } returns 10
                shouldThrow<BusinessException> {
                    authService.checkAuthAttemptExceed(authAttempt.phoneNumber, authAttempt.purpose)
                }.errorCode shouldBe ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER
            }
        }

        context("generateTokenBySignIn") {
            test("액세스 토큰을 발급한다.") {
                val accessToken = "<ACCESS_TOKEN>"
                every { jwtProvider.generate(any()) } returns accessToken
                val actual = authService.generateAccessTokenBySignIn(1L, "admin@everyonewaiter.com")
                actual shouldBe accessToken
            }
        }

        context("generateCode") {
            test("인증 번호를 생성한다.") {
                every { authCodeRepository.save(any()) } just runs
                every { authAttemptRepository.increment(any()) } just runs
                every { applicationEventPublisher.publishEvent(any(AuthCodeCreateEvent::class)) } just runs

                authService.generateCode("01044591812", AuthPurpose.SIGN_UP)

                verify { authCodeRepository.save(any()) }
                verify { authAttemptRepository.increment(any()) }
                verify { applicationEventPublisher.publishEvent(any(AuthCodeCreateEvent::class)) }
            }
        }

        context("verifyCode") {
            val authCode = AuthCode("01044591812", 123456)

            test("인증 번호를 검증한다.") {
                every { authCodeRepository.find(any(AuthCode::class)) } returns authCode.code
                every { authSuccessRepository.save(any()) } just runs
                shouldNotThrowAny {
                    authService.verifyCode(VerifyAuthCode.Request(authCode.phoneNumber, authCode.code))
                }
                verify { authSuccessRepository.save(AuthSuccess(authCode.phoneNumber)) }
            }

            test("인증 번호를 찾지 못한 경우 예외가 발생한다.") {
                every { authCodeRepository.find(any(AuthCode::class)) } returns null
                shouldThrow<BusinessException> {
                    authService.verifyCode(VerifyAuthCode.Request(authCode.phoneNumber, authCode.code))
                }.errorCode shouldBe ErrorCode.EXPIRED_VERIFICATION_CODE
            }

            test("인증 번호가 일치 하지 않는 경우 예외가 발생한다.") {
                every { authCodeRepository.find(any(AuthCode::class)) } returns 123456
                shouldThrow<BusinessException> {
                    authService.verifyCode(VerifyAuthCode.Request(authCode.phoneNumber, 654321))
                }.errorCode shouldBe ErrorCode.UNMATCHED_VERIFICATION_CODE
            }
        }

        context("sendAuthMail") {
            test("이메일 인증 메일을 발송한다.") {
                every { applicationEventPublisher.publishEvent(any(AuthMailSendEvent::class)) } just runs
                authService.sendAuthMail("admin@everyonewaiter.com")
                verify { applicationEventPublisher.publishEvent(any(AuthMailSendEvent::class)) }
            }
        }

        context("verifyAuthMail") {
            val accessToken = "<ACCESS_TOKEN>"

            test("이메일 인증 토큰 검증에 성공하면 이메일 주소를 반환한다.") {
                val subject = "admin@everyonewaiter.com"
                val payload = JwtPayload(0L, subject)
                every { jwtProvider.decode(accessToken) } returns Result.success(payload)
                val actual = authService.verifyAuthMail(accessToken)
                actual shouldBe subject
            }

            test("토큰 검증에 실패하면 예외가 발생한다.") {
                every { jwtProvider.decode(accessToken) } returns Result.failure(IllegalArgumentException("Invalid Token"))
                shouldThrow<BusinessException> {
                    authService.verifyAuthMail(accessToken)
                }.errorCode shouldBe ErrorCode.EXPIRED_VERIFICATION_EMAIL
            }

            test("이메일 인증 전용 토큰이 아니라면 예외가 발생한다.") {
                val payload = JwtPayload(1L, "admin@everyonewaiter.com")
                every { jwtProvider.decode(accessToken) } returns Result.success(payload)
                shouldThrow<BusinessException> {
                    authService.verifyAuthMail(accessToken)
                }.errorCode shouldBe ErrorCode.EXPIRED_VERIFICATION_EMAIL
            }
        }
    })
