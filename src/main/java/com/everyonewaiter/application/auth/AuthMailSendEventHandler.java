package com.everyonewaiter.application.auth;

import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.domain.notification.service.MailSender;
import com.everyonewaiter.domain.notification.service.request.MailSend;
import com.everyonewaiter.global.config.ClientUrlRegistry;
import com.everyonewaiter.global.security.JwtFixedId;
import com.everyonewaiter.global.security.JwtPayload;
import com.everyonewaiter.global.security.JwtProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class AuthMailSendEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthMailSendEventHandler.class);

  private final ClientUrlRegistry clientUrlRegistry;
  private final MailSender mailSender;
  private final JwtProvider jwtProvider;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(AuthMailSendEvent event) {
    LOGGER.info("[이메일 인증 메일 전송 이벤트] email: {}", event.email());

    JwtPayload payload = new JwtPayload(JwtFixedId.VERIFICATION_EMAIL, event.email());
    String authToken = jwtProvider.generate(payload, Duration.ofDays(1));
    String authUrl = clientUrlRegistry.getUrls().getFirst()
        + "/auth/mail?email="
        + event.email()
        + "&token="
        + authToken;

    MailSend request = MailSend.builder()
        .to(event.email())
        .templateName("email-authentication")
        .subject("[모두의 웨이터] 이메일 인증 안내드립니다.")
        .variable("authenticationUrl", authUrl)
        .build();
    mailSender.sendTo(request);
  }

}
