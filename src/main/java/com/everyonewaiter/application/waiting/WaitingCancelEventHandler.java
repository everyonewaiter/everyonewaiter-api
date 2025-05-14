package com.everyonewaiter.application.waiting;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.service.AlimTalkSender;
import com.everyonewaiter.domain.waiting.event.WaitingCancelByCustomerEvent;
import com.everyonewaiter.domain.waiting.event.WaitingCancelByStoreEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class WaitingCancelEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(WaitingCancelEventHandler.class);

  private final AlimTalkSender alimTalkSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(WaitingCancelByStoreEvent event) {
    LOGGER.info("[웨이팅 취소 (매장) 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    String content = """
        안녕하세요.
        %s입니다.
        \s
        대기번호 %s번 고객님 매장 미입장으로 인해 웨이팅 등록이 취소되었습니다.
        """
        .trim()
        .formatted(event.storeName(), event.number());
    AlimTalkMessage message = new AlimTalkMessage(event.phoneNumber(), content);

    alimTalkSender.sendTo("WaitingStoreCancel", message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(WaitingCancelByCustomerEvent event) {
    LOGGER.info("[웨이팅 취소 (손님) 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    String content = """
        안녕하세요.
        %s입니다.
        \s
        대기번호 %s번 고객님 웨이팅 등록이 정상적으로 취소되었습니다.
        \s
        오늘도 좋은 하루 보내세요.
        """
        .trim()
        .formatted(event.storeName(), event.number());
    AlimTalkMessage message = new AlimTalkMessage(event.phoneNumber(), content);

    alimTalkSender.sendTo("WaitingCustomerCancel", message);
  }

}
