package com.everyonewaiter.common.sse.extension

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

/**
 * SSE 이벤트를 전송합니다.
 * 이벤트 전송 실패 시 이미터를 완료 처리합니다.
 */
fun SseEmitter.sendEvent(
    key: String,
    event: String,
    name: String = "sse",
) {
    runCatching {
        send(
            SseEmitter
                .event()
                .id(key)
                .name(name)
                .data(event),
        )
    }.onFailure { completeWithError(it) }
}
