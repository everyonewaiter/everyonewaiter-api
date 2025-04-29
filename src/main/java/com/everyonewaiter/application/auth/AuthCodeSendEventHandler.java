package com.everyonewaiter.application.auth;

import com.everyonewaiter.domain.auth.event.AuthCodeSendEvent;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.service.AlimTalkSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthCodeSendEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthCodeSendEventHandler.class);

  private final AlimTalkSender alimTalkSender;

  @Async("eventTaskExecutor")
  @EventListener
  public void consume(AuthCodeSendEvent event) {
    LOGGER.info("[휴대폰 인증 번호 전송 이벤트] phone: {}", event.phoneNumber());

    String content = """
        [모두의 웨이터]
        
        인증번호는 [%s]입니다.
        """.trim().formatted(event.code());
    AlimTalkMessage message = new AlimTalkMessage(event.phoneNumber(), content);

    alimTalkSender.sendTo("authenticationCode", message);
  }

}
