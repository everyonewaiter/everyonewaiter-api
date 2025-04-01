package com.everyonewaiter.application.account.service

import com.everyonewaiter.application.account.dto.SignUp
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.repository.AccountRepository
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder

class AccountServiceTest :
    FunSpec({
        val passwordEncoder = mockk<PasswordEncoder>()
        val accountRepository = mockk<AccountRepository>()
        val accountService = AccountService(passwordEncoder, accountRepository)

        context("create") {
            val request = SignUp.Request("admin@everyonewaiter.com", "@password1", "01044591812")
            val account = Account.create(request.email, request.password, request.phoneNumber)

            test("계정을 생성한다.") {
                every { accountRepository.existsByEmail(request.email) } returns false
                every { accountRepository.existsByPhone(request.phoneNumber) } returns false
                every { passwordEncoder.encode(request.password) } returns "<ENCODED_PASSWORD>"
                every { accountRepository.save(any()) } returns account
                val actual = accountService.create(request)
                actual shouldBe account.id
                verify { accountRepository.save(any()) }
            }

            test("이메일이 이미 사용 중이라면 예외가 발생한다.") {
                every { accountRepository.existsByEmail(request.email) } returns true
                shouldThrow<BusinessException> {
                    accountService.create(request)
                }.errorCode shouldBe ErrorCode.ALREADY_USE_EMAIL
            }

            test("휴대폰 번호가 이미 사용 중이라면 예외가 발생한다.") {
                every { accountRepository.existsByEmail(request.email) } returns false
                every { accountRepository.existsByPhone(request.phoneNumber) } returns true
                shouldThrow<BusinessException> {
                    accountService.create(request)
                }.errorCode shouldBe ErrorCode.ALREADY_USE_PHONE_NUMBER
            }
        }

        context("checkPhoneNumberNotInUse") {
            test("휴대폰 번호를 사용 중이지 않으면 예외를 발생시키지 않는다.") {
                val phoneNumber = "01012345678"
                every { accountRepository.existsByPhone(phoneNumber) } returns false
                shouldNotThrowAny { accountService.checkPhoneNumberNotInUse(phoneNumber) }
            }

            test("휴대폰 번호를 사용중이라면 예외가 발생한다.") {
                val phoneNumber = "01012345678"
                every { accountRepository.existsByPhone(phoneNumber) } returns true
                shouldThrow<BusinessException> {
                    accountService.checkPhoneNumberNotInUse(phoneNumber)
                }.errorCode shouldBe ErrorCode.ALREADY_USE_PHONE_NUMBER
            }
        }
    })
