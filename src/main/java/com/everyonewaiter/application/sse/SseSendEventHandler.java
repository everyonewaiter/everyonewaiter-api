package com.everyonewaiter.application.sse;

import com.everyonewaiter.application.sse.provided.SseSender;
import com.everyonewaiter.domain.sse.SseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class SseSendEventHandler {

  private final SseSender sseSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(SseEvent event) {
    sseSender.send(event.storeId(), event);
  }

}
