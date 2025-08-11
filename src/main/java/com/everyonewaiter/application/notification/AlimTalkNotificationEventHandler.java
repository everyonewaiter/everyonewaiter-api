package com.everyonewaiter.application.notification;

import static com.everyonewaiter.domain.notification.AlimTalkTemplate.AUTHENTICATION_CODE;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_CUSTOMER_CALL;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_CUSTOMER_CANCEL;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_REGISTRATION;
import static com.everyonewaiter.domain.notification.AlimTalkTemplate.WAITING_STORE_CANCEL;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.CHECK_MY_TURN;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.MENU_PREVIEW;
import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.WAITING_CANCEL;

import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.auth.event.AuthCodeSendEvent;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.waiting.event.WaitingCancelByCustomerEvent;
import com.everyonewaiter.domain.waiting.event.WaitingCancelByStoreEvent;
import com.everyonewaiter.domain.waiting.event.WaitingCustomerCallEvent;
import com.everyonewaiter.domain.waiting.event.WaitingRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class AlimTalkNotificationEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AlimTalkNotificationEventHandler.class);

  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @EventListener
  public void consumeAuthCodeSendEvent(AuthCodeSendEvent event) {
    LOGGER.info("[휴대폰 인증 번호 전송 이벤트] phone: {}", event.phoneNumber());

    AlimTalkMessage message = new AlimTalkMessage(
        new PhoneNumber(event.phoneNumber()),
        AUTHENTICATION_CODE,
        event.code()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeWaitingRegistrationEvent(WaitingRegistrationEvent event) {
    LOGGER.info("[웨이팅 등록 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    AlimTalkMessage message = new AlimTalkMessage(
        new PhoneNumber(event.phoneNumber()),
        WAITING_REGISTRATION,
        event.storeName(),
        event.adult(),
        event.infant(),
        event.number(),
        event.storeLandline(),
        event.storeName()
    );
    message.addButton(WAITING_CANCEL, event.storeId(), event.accessKey());
    message.addButton(CHECK_MY_TURN, event.storeId(), event.accessKey());
    message.addButton(MENU_PREVIEW, event.storeId());

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeWaitingCustomerCallEvent(WaitingCustomerCallEvent event) {
    LOGGER.info("[웨이팅 손님 호출 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    AlimTalkMessage message = new AlimTalkMessage(
        new PhoneNumber(event.phoneNumber()),
        WAITING_CUSTOMER_CALL,
        event.storeName(),
        event.number()
    );
    message.addButton(WAITING_CANCEL, event.storeId(), event.accessKey());

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeWaitingCancelByCustomerEvent(WaitingCancelByCustomerEvent event) {
    LOGGER.info("[웨이팅 취소 (손님) 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    AlimTalkMessage message = new AlimTalkMessage(
        new PhoneNumber(event.phoneNumber()),
        WAITING_CUSTOMER_CANCEL,
        event.storeName(),
        event.number()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeWaitingCancelByStoreEvent(WaitingCancelByStoreEvent event) {
    LOGGER.info("[웨이팅 취소 (매장) 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    AlimTalkMessage message = new AlimTalkMessage(
        new PhoneNumber(event.phoneNumber()),
        WAITING_STORE_CANCEL,
        event.storeName(),
        event.number()
    );

    notificationSender.sendAlimTalkOneToOne(message);
  }

}
