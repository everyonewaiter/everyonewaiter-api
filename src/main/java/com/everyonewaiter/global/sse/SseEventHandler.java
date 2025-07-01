package com.everyonewaiter.global.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class SseEventHandler {

  private final SseService sseService;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(SseEvent event) {
    sseService.sendEvent(event.storeId().toString(), event);
  }

}
