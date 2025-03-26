package com.everyonewaiter.common.sse

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

class SseManagerTest :
    FunSpec({
        val emitterRepository = mockk<SseEmitterRepository>()
        val eventRepository = mockk<SseEventRepository>()
        val sseManager = SseManager(
            objectMapper = ObjectMapper(),
            sseEmitterRepository = emitterRepository,
            sseEventRepository = eventRepository,
        )

        context("connect") {
            every { emitterRepository.save(any(), any()) } just Runs

            test("SseEmitter를 생성하고 저장한다.") {
                val prefix = "prefix"
                sseManager.connect(prefix, "")
                verify { emitterRepository.save(any(), any()) }
            }
        }

        context("sendEvent") {
            every { emitterRepository.findAllByScanKey(any()) } returns emptyMap()
            every { eventRepository.save(any(), any()) } just Runs

            test("이벤트 전송 시 이벤트를 저장한다.") {
                val prefix = "prefix"
                val event = "event"
                sseManager.connect(prefix, "")
                sseManager.sendEvent(prefix, event)
                verify {
                    emitterRepository.findAllByScanKey("$prefix::emitter::")
                    eventRepository.save(any(), event)
                }
            }
        }
    })
