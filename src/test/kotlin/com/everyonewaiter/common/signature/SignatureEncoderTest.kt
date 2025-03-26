package com.everyonewaiter.common.signature

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SignatureEncoderTest :
    FunSpec({
        val signatureEncoder = SignatureEncoder()

        context("encode") {
            test("평문 문자열을 서명한다.") {
                val plainText = "Hello, World!"
                val secretKey = "secret"
                val actual = signatureEncoder.encode(plainText, secretKey)
                actual.isSuccess shouldBe true
            }
        }

        context("matches") {
            test("서명된 문자열을 검증한다.") {
                val plainText = "Hello, World!"
                val secretKey = "secret"
                val encodedText = signatureEncoder.encode(plainText, secretKey).getOrThrow()
                signatureEncoder.matches(encodedText, plainText, secretKey) shouldBe true
                signatureEncoder.matches(encodedText, plainText + "invalid", secretKey) shouldBe false
            }
        }
    })
