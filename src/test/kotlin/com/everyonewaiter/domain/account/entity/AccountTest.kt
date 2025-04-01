package com.everyonewaiter.domain.account.entity

import com.everyonewaiter.domain.account.event.AccountCreateEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
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
    })
