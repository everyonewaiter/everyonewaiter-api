package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthSuccess
import com.everyonewaiter.support.RedisTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate

@RedisTest
@Import(AuthSuccessRedisRepository::class)
class AuthSuccessRedisRepositoryTest(
    private val redisTemplate: RedisTemplate<String, String>,
    private val authPhoneRedisRepository: AuthSuccessRedisRepository,
) : FunSpec({
        val authSuccess = AuthSuccess("01044591812")

        afterContainer { redisTemplate.execute { connection -> connection.serverCommands().flushDb() } }

        context("exists") {
            authPhoneRedisRepository.save(authSuccess)

            test("인증된 휴대폰이 존재하는지 여부를 반환한다.") {
                authPhoneRedisRepository.exists(authSuccess) shouldBe true
                authPhoneRedisRepository.exists(AuthSuccess("01012345678")) shouldBe false
            }
        }
    })
