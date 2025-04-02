package com.everyonewaiter.common.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Encoders
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class JwtProviderTest :
    FunSpec({
        val jwtProvider = JwtProvider(
            Encoders.BASE64.encode(
                Jwts.SIG.HS256
                    .key()
                    .build()
                    .encoded,
            ),
        )

        context("generate") {
            test("토큰을 생성하고 생성한 토큰을 불러올 수 있다.") {
                val payload = JwtPayload(
                    id = 123456789,
                    subject = "admin@everyonewaiter.com",
                )
                val token = jwtProvider.generate(payload)
                val actual = jwtProvider.decode(token)
                actual.isSuccess shouldBe true
                actual.getOrThrow() shouldBe payload
            }
        }

        context("decode") {
            test("유효하지 않은 토큰을 불러오면 예외가 발생한다.") {
                val payload = JwtPayload(
                    id = 123456789,
                    subject = "admin@everyonewaiter.com",
                )
                val token = jwtProvider.generate(payload)
                val actual = jwtProvider.decode(token + "invalid")
                actual.isFailure shouldBe true
            }
        }
    })
