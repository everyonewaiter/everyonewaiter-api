package com.everyonewaiter.application.notification;

import static com.everyonewaiter.domain.notification.EmailTemplate.EMAIL_AUTHENTICATION;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_APPROVE;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_REJECT;
import static com.everyonewaiter.domain.support.ClientUri.AUTH_EMAIL;
import static com.everyonewaiter.domain.support.ClientUri.STORE_REGISTRATION;

import com.everyonewaiter.application.auth.required.JwtEncoder;
import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.auth.JwtFixedId;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.domain.notification.TemplateEmail;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.store.event.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.event.RegistrationRejectEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class EmailNotificationEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationEventHandler.class);

  private final JwtEncoder jwtEncoder;
  private final AccountRepository accountRepository;
  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeAuthMailSendEvent(AuthMailSendEvent event) {
    LOGGER.info("[이메일 인증 메일 전송 이벤트] email: {}", event.email());

    JwtPayload payload = new JwtPayload(JwtFixedId.VERIFICATION_EMAIL, event.email());
    String authToken = jwtEncoder.encode(payload, Duration.ofDays(1));
    String authUri = AUTH_EMAIL.formatted(event.email(), authToken);

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(event.email()),
        "[모두의 웨이터] 이메일 인증 안내드립니다.",
        EMAIL_AUTHENTICATION
    );
    templateEmail.addTemplateVariable("authenticationUrl", authUri);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationApproveEvent(RegistrationApproveEvent event) {
    Long accountId = event.accountId();
    String storeName = event.businessLicense().getName();
    LOGGER.info("[매장 등록 신청 승인 알림 이벤트] accountId: {}, storeName: {}", accountId, storeName);

    Account account = accountRepository.findByIdOrThrow(accountId);

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(account.getEmail()),
        "[모두의 웨이터] 매장 등록 신청이 승인되었습니다.",
        STORE_REGISTRATION_APPROVE
    );
    templateEmail.addTemplateVariable("name", storeName);
    templateEmail.addTemplateVariable("storeRegistrationUrl", STORE_REGISTRATION);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationRejectEvent(RegistrationRejectEvent event) {
    LOGGER.info("[매장 등록 신청 반려 이벤트] storeName: {}", event.storeName());

    Account account = accountRepository.findByIdOrThrow(event.accountId());

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(account.getEmail()),
        "[모두의 웨이터] 매장 등록 신청이 반려되었습니다.",
        STORE_REGISTRATION_REJECT
    );
    templateEmail.addTemplateVariable("name", event.storeName());
    templateEmail.addTemplateVariable("reason", event.rejectReason());
    templateEmail.addTemplateVariable("storeRegistrationUrl", STORE_REGISTRATION);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

}
