package com.everyonewaiter.domain.account.entity

import com.everyonewaiter.domain.account.event.AccountCreateEvent
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe

class AccountTest :
    FunSpec({
        context("create") {
            test("계정을 생성하면 계정 생성 이벤트를 등록한다.") {
                val account = Account.create("admin@everyonewaiter.com", "@password1", "01044591812")
                account.domainEvents.size shouldBe 1
                account.domainEvents shouldContain AccountCreateEvent(account.email)
            }
        }

        context("signIn") {
            val account = Account(
                email = "admin@everyonewaiter.com",
                password = "@password1",
                phoneNumber = "01044591812",
            )

            test("계정에 로그인하면 마지막 로그인 시간을 갱신한다.") {
                val beforeSignIn = account.lastSignIn
                account.signIn()
                account.lastSignIn shouldBeAfter beforeSignIn
            }
        }

        context("activate") {
            val account = Account(
                email = "admin@everyonewaiter.com",
                password = "@password1",
                phoneNumber = "01044591812",
            )

            test("계정의 상태가 비활성이라면 계정의 상태를 활성으로 변경한다.") {
                account.activate()
                account.state shouldBe AccountState.ACTIVE
            }

            test("계정의 상태가 비활성이 아닌 경우 예외가 발생한다.") {
                val copied = account.copy(state = AccountState.DELETE)
                shouldThrow<BusinessException> {
                    copied.activate()
                }.errorCode shouldBe ErrorCode.ALREADY_VERIFIED_EMAIL
            }
        }
    })
