package com.everyonewaiter.application.contact;

import com.everyonewaiter.domain.contact.event.ContactCreateEvent;
import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.DiscordField;
import com.everyonewaiter.domain.notification.service.DiscordSender;
import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class ContactCreateEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContactCreateEventHandler.class);

  private final DiscordSender discordSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(ContactCreateEvent event) {
    LOGGER.info("[서비스 도입 문의 이벤트] name: {}", event.name());

    DiscordEmbed embed = DiscordEmbed.builder()
        .title("서비스 도입 문의 이벤트")
        .description(event.name() + " 사장님께서 서비스 도입을 문의하셨습니다!")
        .color(DiscordColor.GREEN.getValue())
        .field(new DiscordField("사업자 번호", event.license()))
        .field(new DiscordField("휴대폰 번호", event.phoneNumber()))
        .build();
    DiscordMessageSend request = new DiscordMessageSend(embed);

    discordSender.send(request);
  }

}
