package com.everyonewaiter.common.sse.repository

import com.everyonewaiter.common.sse.SseEmitterRepository
import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Repository
internal class SseEmitterInMemoryRepository : SseEmitterRepository {
    private val emitters: MutableMap<String, SseEmitter> = ConcurrentHashMap()

    override fun save(
        key: String,
        emitter: SseEmitter,
    ) {
        emitters[key] = emitter
    }

    override fun findAllByScanKey(scanKey: String): Map<String, SseEmitter> =
        emitters.entries
            .filter { entry -> entry.key.startsWith(scanKey) }
            .associate { entry -> entry.key to entry.value }

    override fun delete(key: String) {
        emitters.remove(key)
    }
}
