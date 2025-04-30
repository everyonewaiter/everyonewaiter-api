package com.everyonewaiter.application.store;

import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.DiscordField;
import com.everyonewaiter.domain.notification.service.DiscordSender;
import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import com.everyonewaiter.domain.store.event.RegistrationReapplyEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class RegistrationReapplyEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RegistrationReapplyEventHandler.class);

  private final DiscordSender discordSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(RegistrationReapplyEvent event) {
    LOGGER.info("[매장 등록 재신청 알림 이벤트] storeName: {}", event.storeName());

    DiscordEmbed embed = DiscordEmbed.builder()
        .title("매장 등록 재신청 이벤트")
        .description(event.storeName() + " 사장님께서 매장 등록을 재신청하셨습니다!")
        .color(DiscordColor.DARK_BLUE.getValue())
        .field(new DiscordField("기존 거절 사유", event.rejectReason()))
        .build();
    DiscordMessageSend request = new DiscordMessageSend(embed);

    discordSender.send(request);
  }

}
