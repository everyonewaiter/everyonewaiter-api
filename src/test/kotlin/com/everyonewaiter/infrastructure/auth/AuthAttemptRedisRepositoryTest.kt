package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthAttempt
import com.everyonewaiter.domain.auth.entity.AuthPurpose
import com.everyonewaiter.support.RedisTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate

@RedisTest
@Import(AuthAttemptRedisRepository::class)
class AuthAttemptRedisRepositoryTest(
    private val redisTemplate: RedisTemplate<String, String>,
    private val authAttemptRedisRepository: AuthAttemptRedisRepository,
) : FunSpec({
        val authAttempt = AuthAttempt(
            phoneNumber = "01044591812",
            purpose = AuthPurpose.CREATE_DEVICE,
            count = 3,
        )

        afterContainer { redisTemplate.execute { connection -> connection.serverCommands().flushDb() } }

        context("find") {
            authAttemptRedisRepository.save(authAttempt)

            test("인증 시도 횟수를 조회한다.") {
                authAttemptRedisRepository.find(authAttempt) shouldBe 3
            }

            test("인증 시도 횟수를 조회하지 못하면 0을 반환한다.") {
                val copied = authAttempt.copy(phoneNumber = "01012345678")
                authAttemptRedisRepository.find(copied) shouldBe 0
            }
        }

        context("increment") {
            authAttemptRedisRepository.save(authAttempt)

            test("인증 시도 횟수를 증가시킨다.") {
                authAttemptRedisRepository.increment(authAttempt)
                redisTemplate.opsForValue()[authAttempt.key]?.toInt() shouldBe authAttempt.count + 1
            }

            test("최초 인증 시도 횟수 증가라면 만료 시간을 설정한다.") {
                val copied = authAttempt.copy(phoneNumber = "01012345678")
                authAttemptRedisRepository.increment(copied)
                redisTemplate.opsForValue()[copied.key]?.toInt() shouldBe 1
                redisTemplate.getExpire(copied.key) shouldNotBe -1
            }
        }
    })
