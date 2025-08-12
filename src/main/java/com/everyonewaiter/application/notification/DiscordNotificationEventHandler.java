package com.everyonewaiter.application.notification;

import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.account.AccountCreateEvent;
import com.everyonewaiter.domain.contact.ContactCreateEvent;
import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import com.everyonewaiter.domain.notification.DiscordField;
import com.everyonewaiter.domain.store.event.RegistrationApplyEvent;
import com.everyonewaiter.domain.store.event.RegistrationReapplyEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class DiscordNotificationEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DiscordNotificationEventHandler.class);

  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeContactCreateEvent(ContactCreateEvent event) {
    LOGGER.info("[서비스 도입 문의 이벤트] storeName: {}", event.storeName());

    DiscordEmbed embed = new DiscordEmbed(
        DiscordColor.GREEN,
        "서비스 도입 문의 이벤트",
        event.storeName() + " 사장님께서 서비스 도입을 문의하셨습니다!"
    );
    embed.addField(new DiscordField("사업자 등록 번호", event.license().value()));
    embed.addField(new DiscordField("휴대폰 번호", event.phoneNumber().value()));

    notificationSender.sendDiscord(new DiscordEmbeds(embed));
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeAccountCreateEvent(AccountCreateEvent event) {
    LOGGER.info("[계정 생성 이벤트] email: {}", event.email().address());

    DiscordEmbed embed = new DiscordEmbed(
        DiscordColor.GREEN,
        "계정 생성 이벤트",
        event.email().address() + "님이 계정을 생성하였습니다!"
    );

    notificationSender.sendDiscord(new DiscordEmbeds(embed));
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationApplyEvent(RegistrationApplyEvent event) {
    LOGGER.info("[매장 등록 신청 이벤트] storeName: {}", event.storeName());

    DiscordEmbed embed = new DiscordEmbed(
        DiscordColor.BLUE,
        "매장 등록 신청 이벤트",
        event.storeName() + " 사장님께서 매장 등록을 신청하셨습니다!"
    );

    notificationSender.sendDiscord(new DiscordEmbeds(embed));
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationReapplyEvent(RegistrationReapplyEvent event) {
    LOGGER.info("[매장 등록 재신청 알림 이벤트] storeName: {}", event.storeName());

    DiscordEmbed embed = new DiscordEmbed(
        DiscordColor.DARK_BLUE,
        "매장 등록 재신청 이벤트",
        event.storeName() + " 사장님께서 매장 등록을 재신청하셨습니다!"
    );
    embed.addField(new DiscordField("기존 거절 사유", event.rejectReason()));

    notificationSender.sendDiscord(new DiscordEmbeds(embed));
  }

}
