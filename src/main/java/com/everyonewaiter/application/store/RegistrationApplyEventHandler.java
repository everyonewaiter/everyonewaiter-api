package com.everyonewaiter.application.store;

import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.service.DiscordSender;
import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import com.everyonewaiter.domain.store.event.RegistrationApplyEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class RegistrationApplyEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApplyEventHandler.class);

  private final DiscordSender discordSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(RegistrationApplyEvent event) {
    LOGGER.info("[매장 등록 신청 이벤트] storeName: {}", event.storeName());

    DiscordEmbed embed = DiscordEmbed.builder()
        .title("매장 등록 신청 이벤트")
        .description(event.storeName() + " 사장님께서 매장 등록을 신청하셨습니다!")
        .color(DiscordColor.BLUE.getValue())
        .build();
    DiscordMessageSend request = new DiscordMessageSend(embed);

    discordSender.send(request);
  }

}
