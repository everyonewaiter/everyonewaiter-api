package com.everyonewaiter.application.notification;

import static com.everyonewaiter.domain.notification.AlimTalkTemplate.AUTHENTICATION_CODE;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_CUSTOMER_CALL;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_CUSTOMER_CANCEL;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_REGISTRATION;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_STORE_CANCEL;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.CHECK_MY_TURN;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.MENU_PREVIEW;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.WAITING_CANCEL;
import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.auth.AuthCodeSendEvent;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingCancelByCustomerEvent;
import com.everyonewaiter.domain.waiting.WaitingCancelByStoreEvent;
import com.everyonewaiter.domain.waiting.WaitingCustomerCallEvent;
import com.everyonewaiter.domain.waiting.WaitingRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class AlimTalkNotificationEventHandler {

  private static final Logger LOGGER = getLogger(AlimTalkNotificationEventHandler.class);

  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @EventListener
  public void handle(AuthCodeSendEvent event) {
    LOGGER.info("[휴대폰 인증 번호 전송 이벤트] phone: {}", event.phoneNumber());

    AlimTalkMessage message = new AlimTalkMessage(
        AUTHENTICATION_CODE,
        event.phoneNumber(),
        event.code()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(WaitingRegistrationEvent event) {
    Waiting waiting = event.waiting();
    Store store = waiting.getStore();

    LOGGER.info("[웨이팅 등록 이벤트] storeId: {}, storeName: {}",
        store.getId(), store.getDetail().getName());

    AlimTalkMessage message = new AlimTalkMessage(
        WAITING_REGISTRATION,
        waiting.getPhoneNumber(),
        store.getDetail().getName(),
        waiting.getAdult(),
        waiting.getInfant(),
        waiting.getNumber(),
        store.getDetail().getLandline(),
        store.getDetail().getName()
    );
    message.addButton(WAITING_CANCEL, store.getId(), waiting.getAccessKey(), waiting.getNumber());
    message.addButton(CHECK_MY_TURN, store.getId(), waiting.getAccessKey());
    message.addButton(MENU_PREVIEW, store.getId());

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(WaitingCustomerCallEvent event) {
    Waiting waiting = event.waiting();
    Store store = waiting.getStore();

    LOGGER.info("[웨이팅 손님 호출 이벤트] storeId: {}, storeName: {}",
        store.getId(), store.getDetail().getName());

    AlimTalkMessage message = new AlimTalkMessage(
        WAITING_CUSTOMER_CALL,
        waiting.getPhoneNumber(),
        store.getDetail().getName(),
        waiting.getNumber()
    );
    message.addButton(WAITING_CANCEL, store.getId(), waiting.getAccessKey(), waiting.getNumber());

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(WaitingCancelByCustomerEvent event) {
    Waiting waiting = event.waiting();
    Store store = waiting.getStore();

    LOGGER.info("[웨이팅 취소 (손님) 이벤트] storeId: {}, storeName: {}",
        store.getId(), store.getDetail().getName());

    AlimTalkMessage message = new AlimTalkMessage(
        WAITING_CUSTOMER_CANCEL,
        waiting.getPhoneNumber(),
        store.getDetail().getName(),
        waiting.getNumber()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(WaitingCancelByStoreEvent event) {
    Waiting waiting = event.waiting();
    Store store = waiting.getStore();

    LOGGER.info("[웨이팅 취소 (매장) 이벤트] storeId: {}, storeName: {}",
        store.getId(), store.getDetail().getName());

    AlimTalkMessage message = new AlimTalkMessage(
        WAITING_STORE_CANCEL,
        waiting.getPhoneNumber(),
        store.getDetail().getName(),
        waiting.getNumber()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

}
