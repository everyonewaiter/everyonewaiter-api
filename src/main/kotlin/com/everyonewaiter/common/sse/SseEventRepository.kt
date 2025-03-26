package com.everyonewaiter.common.sse

/**
 * SSE 이벤트들을 관리하는 저장소
 */
interface SseEventRepository {
    /**
     * SSE 이벤트를 저장합니다.
     * 기본적으로 저장되는 이벤트의 TTL은 10분 입니다.
     *
     * @param [key] 이벤트를 식별하는 키
     * @param [event] 저장할 이벤트
     */
    fun save(
        key: String,
        event: String,
    )

    /**
     * [scanKey]로 시작하는 키의 모든 SSE 이벤트를 찾습니다.
     *
     * @param [scanKey] key 접두사
     */
    fun findAllByScanKey(scanKey: String): Map<String, String>

    /**
     * SSE 이벤트를 삭제합니다.
     *
     * @param [key] 삭제할 이벤트의 키
     */
    fun delete(key: String)
}
