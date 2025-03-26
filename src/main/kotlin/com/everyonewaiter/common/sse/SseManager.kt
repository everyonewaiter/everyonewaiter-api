package com.everyonewaiter.common.sse

import com.everyonewaiter.common.sse.extension.sendEvent
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

private const val DELIMITER = "::"
private const val CONNECT_MESSAGE = "CONNECTED!"
private const val FIVE_MINUTE_MILLISECONDS = 5L * 60L * 1000L

private val logger = KotlinLogging.logger {}

/**
 * SSE 및 SSE 이벤트를 관리하는 클래스입니다.
 *
 * @property [ObjectMapper] Any 타입의 이벤트를 직렬화하기 위함
 * @property [SseEmitterRepository] SSE 이미터를 저장하고 관리하는 저장소
 * @property [SseEventRepository] SSE 이벤트를 저장하고 관리하는 저장소
 */
@Component
class SseManager(
    private val objectMapper: ObjectMapper,
    private val sseEmitterRepository: SseEmitterRepository,
    private val sseEventRepository: SseEventRepository,
) {
    /**
     * SSE 연결을 위한 SSE 이미터를 생성하고 저장합니다.
     * 이미터를 생성한 후 503 예외를 방지하기 위해 연결 메시지를 전송합니다.
     * [lastEventId]가 빈 문자열이 아닌 경우 유실된 이벤트가 있는지 찾아 전송합니다.
     *
     * @param [prefix] SSE 이미터 생성에 사용할 접두사
     * @param [lastEventId] 마지막 이벤트 ID (재연결인 경우)
     */
    fun connect(
        prefix: String,
        lastEventId: String,
    ): SseEmitter {
        val key = generateKey(prefix, SseKeyType.EMITTER)
        val emitter = createSseEmitter(key)
        sseEmitterRepository.save(key, emitter)
        emitter.sendEvent(key, CONNECT_MESSAGE)

        if (StringUtils.hasText(lastEventId)) {
            sseEventRepository
                .findAllByScanKey(generateScanKey(prefix, SseKeyType.EVENT))
                .entries
                .filter { eventEntry -> lastEventId < eventEntry.key }
                .forEach { eventEntry -> emitter.sendEvent(eventEntry.key, eventEntry.value) }
        }
        return emitter
    }

    private fun createSseEmitter(key: String): SseEmitter {
        val emitter = SseEmitter(FIVE_MINUTE_MILLISECONDS)
        emitter.onCompletion {
            logger.info { "SseEmitter completed. key=$key" }
            sseEmitterRepository.delete(key)
        }
        emitter.onTimeout {
            logger.info { "SseEmitter time out. key=$key" }
            emitter.complete()
        }
        emitter.onError { throwable: Throwable ->
            logger.warn { "SseEmitter error. key=$key throwable=${throwable.message}" }
            emitter.complete()
        }
        return emitter
    }

    /**
     * SSE 이벤트를 저장하고 발행합니다.
     * [event]가 문자열이 아닌 경우 직렬화하여 저장합니다.
     *
     * @param [prefix] SSE 이미터 및 이벤트를 식별하는 접두사
     * @param [event] 발행할 이벤트
     */
    fun sendEvent(
        prefix: String,
        event: Any,
    ) {
        val key = generateKey(prefix, SseKeyType.EVENT)
        val serializedEvent = if (event is String) event else objectMapper.writeValueAsString(event)
        sseEmitterRepository
            .findAllByScanKey(generateScanKey(prefix, SseKeyType.EMITTER))
            .values
            .forEach { sseEmitter -> sseEmitter.sendEvent(key, serializedEvent) }
        sseEventRepository.save(key, serializedEvent)
    }

    private fun generateKey(
        prefix: String,
        type: SseKeyType,
    ): String = "${generateScanKey(prefix, type)}${System.currentTimeMillis()}"

    private fun generateScanKey(
        prefix: String,
        type: SseKeyType,
    ): String = "$prefix$DELIMITER${type.lowerCaseName}$DELIMITER"
}
