package com.everyonewaiter.application.auth.service;

import com.everyonewaiter.application.notification.mail.service.MailService;
import com.everyonewaiter.application.notification.mail.service.request.MailSend;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.global.config.ClientUrlRegistry;
import java.time.Duration;
import java.util.Map;
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
  private final AuthService authService;
  private final MailService mailService;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(AuthMailSendEvent event) {
    LOGGER.info("[이메일 인증 메일 전송 이벤트] email: {}", event.email());

    JwtPayload payload = new JwtPayload(0L, event.email());
    String authToken = authService.generateToken(payload, Duration.ofDays(1));
    String authUrl = clientUrlRegistry.getUrls().getFirst()
        + "/auth/mail?email="
        + event.email()
        + "&token="
        + authToken;

    mailService.sendMail(
        MailSend.of(
            event.email(),
            "email-authentication",
            "[모두의 웨이터] 이메일 인증 안내드립니다.",
            Map.of("authenticationUrl", authUrl)
        )
    );
  }

}
