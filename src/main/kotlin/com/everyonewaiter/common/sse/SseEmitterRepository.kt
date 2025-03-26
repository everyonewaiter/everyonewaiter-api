package com.everyonewaiter.common.sse

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

/**
 * SSE 이미터들을 관리하는 저장소
 */
interface SseEmitterRepository {
    /**
     * SSE 이미터를 저장합니다.
     *
     * @param [key] 이미터를 식별하는 키
     * @param [emitter] 저장할 이미터
     */
    fun save(
        key: String,
        emitter: SseEmitter,
    )

    /**
     * [scanKey]로 시작하는 키의 모든 SSE 이미터를 찾습니다.
     *
     * @param [scanKey] key 접두사
     */
    fun findAllByScanKey(scanKey: String): Map<String, SseEmitter>

    /**
     * SSE 이미터를 삭제합니다.
     *
     * @param [key] 삭제할 이미터의 키
     */
    fun delete(key: String)
}
