package com.everyonewaiter.application.account.service

import com.everyonewaiter.application.account.dto.SignIn
import com.everyonewaiter.application.account.dto.SignUp
import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.entity.AccountStatus
import com.everyonewaiter.domain.account.repository.AccountRepository
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant

class AccountServiceTest :
    FunSpec({
        val passwordEncoder = mockk<PasswordEncoder>()
        val accountRepository = mockk<AccountRepository>()
        val accountService = AccountService(passwordEncoder, accountRepository)

        context("create") {
            val request = SignUp.Request("admin@everyonewaiter.com", "@password1", "01044591812")
            val account = Account(
                email = request.email,
                password = request.password,
                phoneNumber = request.phoneNumber,
            )

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

        context("checkCanSendAuthMail") {
            val account = Account(
                email = "admin@everyonewaiter.com",
                password = "@password1",
                phoneNumber = "01044591812",
            )

            test("이메일로 계정을 찾을 수 있고, 계정이 비활성 상태라면 예외가 발생하지 않는다.") {
                every { accountRepository.findByEmail(account.email) } returns account
                shouldNotThrowAny { accountService.checkCanSendAuthMail(account.email) }
            }

            test("이메일로 계정을 찾을 수 없다면 예외가 발생한다.") {
                every { accountRepository.findByEmail(account.email) } returns null
                shouldThrow<BusinessException> {
                    accountService.checkCanSendAuthMail(account.email)
                }.errorCode shouldBe ErrorCode.ACCOUNT_NOT_FOUND
            }

            test("계정이 비활성 상태가 아니라면 예외가 발생한다.") {
                val copied = account.copy(status = AccountStatus.ACTIVE)
                every { accountRepository.findByEmail(copied.email) } returns copied
                shouldThrow<BusinessException> {
                    accountService.checkCanSendAuthMail(copied.email)
                }.errorCode shouldBe ErrorCode.ALREADY_VERIFIED_EMAIL
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

        context("signIn") {
            val request = SignIn.Request("admin@everyonewaiter.com", "@password1")
            val account = Account(
                email = request.email,
                password = request.password,
                phoneNumber = "01044591812",
            )

            test("로그인하면 마지막 로그인 시간이 갱신된다.") {
                val lastSignIn = Instant.ofEpochMilli(0)
                val copied = account.copy(status = AccountStatus.ACTIVE, lastSignIn = lastSignIn)
                every { accountRepository.findByEmail(request.email) } returns copied
                every { passwordEncoder.matches(request.password, copied.password) } returns true
                every { accountRepository.save(copied) } returns copied
                accountService.signIn(request)
                copied.lastSignIn shouldBeAfter lastSignIn
                verify { accountRepository.save(copied) }
            }

            test("이메일로 계정을 찾지 못하면 예외가 발생한다.") {
                every { accountRepository.findByEmail(request.email) } returns null
                shouldThrow<BusinessException> {
                    accountService.signIn(request)
                }.errorCode shouldBe ErrorCode.FAILED_SIGN_IN
            }

            test("계정이 비활성 상태라면 예외가 발생한다.") {
                every { accountRepository.findByEmail(request.email) } returns account
                shouldThrow<BusinessException> {
                    accountService.signIn(request)
                }.errorCode shouldBe ErrorCode.FAILED_SIGN_IN
            }

            test("비밀번호가 일치하지 않으면 예외가 발생한다.") {
                val copied = account.copy(status = AccountStatus.ACTIVE)
                every { accountRepository.findByEmail(request.email) } returns copied
                every { passwordEncoder.matches(request.password, account.password) } returns false
                shouldThrow<BusinessException> {
                    accountService.signIn(request)
                }.errorCode shouldBe ErrorCode.FAILED_SIGN_IN
            }
        }

        context("activate") {
            val account = Account(
                id = Tsid.nextLong(),
                email = "admin@everyonewaiter.com",
                password = "@password1",
                phoneNumber = "01044591812",
            )

            test("계정을 활성화 상태로 변경한다.") {
                every { accountRepository.findByEmail(account.email) } returns account
                every { accountRepository.save(account) } returns account
                accountService.activate(account.email)
                account.status shouldBe AccountStatus.ACTIVE
                verify { accountRepository.save(account) }
            }

            test("이메일로 계정을 찾을 수 없다면 예외가 발생한다.") {
                val copied = account.copy(status = AccountStatus.INACTIVE)
                every { accountRepository.findByEmail(copied.email) } returns null
                shouldThrow<BusinessException> {
                    accountService.activate(copied.email)
                }.errorCode shouldBe ErrorCode.ACCOUNT_NOT_FOUND
                copied.status shouldBe AccountStatus.INACTIVE
            }

            test("계정이 이미 활성화 상태라면 예외가 발생한다.") {
                val copied = account.copy(status = AccountStatus.ACTIVE)
                every { accountRepository.findByEmail(copied.email) } returns copied
                shouldThrow<BusinessException> {
                    accountService.activate(copied.email)
                }.errorCode shouldBe ErrorCode.ALREADY_VERIFIED_EMAIL
            }
        }
    })
