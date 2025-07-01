package com.everyonewaiter.global.sse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class SseEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SseEventHandler.class);

  private final SseService sseService;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(SseEvent event) {
    LOGGER.info("[SSE 전송 이벤트] 매장 ID: {}, 카테고리: {}", event.storeId(), event.category());
    sseService.sendEvent(event.storeId().toString(), event);
  }

}
