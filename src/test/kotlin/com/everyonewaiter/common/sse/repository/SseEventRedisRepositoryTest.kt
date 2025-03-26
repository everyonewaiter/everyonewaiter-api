package com.everyonewaiter.common.sse.repository

import com.everyonewaiter.common.sse.SseEventRepository
import com.everyonewaiter.support.RedisTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate

@RedisTest
@Import(SseEventRedisRepository::class)
class SseEventRedisRepositoryTest(
    private val redisTemplate: RedisTemplate<String, String>,
    private val eventRepository: SseEventRepository,
) : FunSpec({
        afterEach { redisTemplate.execute { connection -> connection.serverCommands().flushDb() } }

        context("save") {
            test("이벤트를 저장한다.") {
                val key = "prefix::event::1234"
                val event = "event"
                eventRepository.save(key, event)
                eventRepository.findAllByScanKey(key) shouldBe mapOf(key to event)
            }
        }

        context("findAllByScanKey") {
            test("scanKey로 시작하는 키의 모든 이벤트를 찾는다.") {
                val repeatCount = 100
                val scanKey = "prefix::event::"
                val event = "event"
                for (i in 1..repeatCount) {
                    eventRepository.save("$scanKey$i", "$event$i")
                }
                eventRepository.findAllByScanKey(scanKey).size shouldBe repeatCount
            }
        }

        context("delete") {
            test("이벤트를 삭제한다.") {
                val key = "prefix::event::1234"
                val event = "event"
                eventRepository.save(key, event)
                eventRepository.findAllByScanKey(key) shouldBe mapOf(key to event)
                eventRepository.delete(key)
                eventRepository.findAllByScanKey(key) shouldBe emptyMap()
            }
        }
    })
