package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthCode
import com.everyonewaiter.support.RedisTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate

@RedisTest
@Import(AuthCodeRedisRepository::class)
class AuthCodeRedisRepositoryTest(
    private val redisTemplate: RedisTemplate<String, String>,
    private val authCodeRedisRepository: AuthCodeRedisRepository,
) : FunSpec({
        val authCode = AuthCode("01044591812", 123456)

        afterContainer { redisTemplate.execute { connection -> connection.serverCommands().flushDb() } }

        context("find") {
            authCodeRedisRepository.save(authCode)

            test("인증 번호를 조회한다.") {
                authCodeRedisRepository.find(authCode) shouldBe 123456
            }

            test("인증 번호를 조회하지 못하면 NULL을 반환한다.") {
                val copied = authCode.copy(phoneNumber = "01012345678")
                authCodeRedisRepository.find(copied) shouldBe null
            }
        }
    })
