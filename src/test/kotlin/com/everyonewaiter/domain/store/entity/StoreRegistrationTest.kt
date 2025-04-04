package com.everyonewaiter.domain.store.entity

import com.everyonewaiter.domain.store.event.StoreRegistrationApplyEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class StoreRegistrationTest :
    FunSpec({
        context("create") {
            test("매장 등록을 신청하면 매장 등록 신청 이벤트를 등록한다.") {
                val registration = StoreRegistration.create(
                    accountId = 1L,
                    licenseInformation = BusinessLicenseInformation(
                        name = "모웨 1호점",
                        ceoName = "홍길동",
                        address = "서울시 강남구",
                        landline = "0212345678",
                        license = "111-11-11111",
                        image = "license.webp",
                    ),
                )
                registration.domainEvents.size shouldBe 1
                registration.domainEvents shouldContain StoreRegistrationApplyEvent(registration.licenseInformation.name)
            }
        }
    })
