package com.everyonewaiter.application.account;

import com.everyonewaiter.application.notification.DiscordService;
import com.everyonewaiter.application.notification.request.DiscordMessageSend;
import com.everyonewaiter.domain.account.event.AccountCreateEvent;
import com.everyonewaiter.domain.notification.discord.DiscordColor;
import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class AccountCreateEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountCreateEventHandler.class);

  private final DiscordService discordService;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(AccountCreateEvent event) {
    LOGGER.info("[계정 생성 이벤트] email: {}", event.email());

    DiscordEmbed embed = DiscordEmbed.builder()
        .title("계정 생성 이벤트")
        .description(event.email() + "님이 계정을 생성하였습니다!")
        .color(DiscordColor.GREEN.getValue())
        .build();
    DiscordMessageSend request = new DiscordMessageSend(embed);

    discordService.sendMessage(request);
  }

}
