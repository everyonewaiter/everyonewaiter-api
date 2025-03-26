package com.everyonewaiter.common.sse

/**
 * SSE 이미터 및 이벤트 저장소에서 사용할 키를 구분하기 위한 열거형입니다.
 * 이미터 및 이벤트 저장소에서 사용되는 키의 포맷이 동일하기 때문에 구분이 필요합니다.
 */
enum class SseKeyType {
    EMITTER,
    EVENT,
    ;

    val lowerCaseName: String
        get() = name.lowercase()
}
