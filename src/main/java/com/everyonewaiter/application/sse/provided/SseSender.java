package com.everyonewaiter.application.sse.provided;

import com.everyonewaiter.domain.sse.SseEvent;

public interface SseSender {

  void send(String prefix, SseEvent sseEvent);

}
