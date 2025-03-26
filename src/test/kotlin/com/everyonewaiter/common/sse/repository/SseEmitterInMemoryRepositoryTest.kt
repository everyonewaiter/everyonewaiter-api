package com.everyonewaiter.common.sse.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

class SseEmitterInMemoryRepositoryTest :
    FunSpec({
        context("save") {
            val repository = SseEmitterInMemoryRepository()

            test("SseEmitter를 저장한다.") {
                val key = "prefix::emitter::1234"
                val sseEmitter = SseEmitter()
                repository.save(key, sseEmitter)
                repository.findAllByScanKey(key) shouldBe mapOf(key to sseEmitter)
            }
        }

        context("findAllByScanKey") {
            val repository = SseEmitterInMemoryRepository()

            test("scanKey로 시작하는 키의 모든 SseEmitter를 찾는다.") {
                val repeatCount = 100
                val scanKey = "prefix::emitter::"
                for (i in 1..repeatCount) {
                    repository.save("$scanKey$i", SseEmitter())
                }
                repository.findAllByScanKey(scanKey).size shouldBe repeatCount
            }
        }

        context("delete") {
            val repository = SseEmitterInMemoryRepository()

            test("SseEmitter를 삭제한다.") {
                val key = "prefix::emitter::1234"
                val sseEmitter = SseEmitter()
                repository.save(key, sseEmitter)
                repository.findAllByScanKey(key) shouldBe mapOf(key to sseEmitter)
                repository.delete(key)
                repository.findAllByScanKey(key) shouldBe emptyMap()
            }
        }
    })
