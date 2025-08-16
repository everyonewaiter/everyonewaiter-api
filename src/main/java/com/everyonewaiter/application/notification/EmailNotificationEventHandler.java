package com.everyonewaiter.application.notification;

import static com.everyonewaiter.domain.notification.EmailTemplate.EMAIL_AUTHENTICATION;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_APPROVE;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_REJECT;
import static com.everyonewaiter.domain.support.ClientUri.AUTH_EMAIL;
import static com.everyonewaiter.domain.support.ClientUri.STORE_REGISTRATION;
import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateEvent;
import com.everyonewaiter.domain.auth.AuthMailSendEvent;
import com.everyonewaiter.domain.auth.JwtFixedId;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.notification.TemplateEmail;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.store.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.RegistrationRejectEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class EmailNotificationEventHandler {

  private static final Logger LOGGER = getLogger(EmailNotificationEventHandler.class);

  private final JwtProvider jwtProvider;
  private final AccountFinder accountFinder;
  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(AccountCreateEvent event) {
    sendAuthMail(event.email());
  }

  @Async("eventTaskExecutor")
  @EventListener
  public void handle(AuthMailSendEvent event) {
    sendAuthMail(event.email());
  }

  private void sendAuthMail(Email email) {
    LOGGER.info("[이메일 인증 메일 전송 이벤트] email: {}", email.address());

    JwtPayload payload = new JwtPayload(JwtFixedId.VERIFICATION_EMAIL_ID, email.address());
    String authToken = jwtProvider.encode(payload, Duration.ofDays(1));
    String authUri = AUTH_EMAIL.formatted(email.address(), authToken);

    TemplateEmail templateEmail = new TemplateEmail(
        EMAIL_AUTHENTICATION,
        email,
        "[모두의 웨이터] 이메일 인증 안내드립니다."
    );
    templateEmail.addTemplateVariable("authenticationUrl", authUri);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(RegistrationApproveEvent event) {
    Long accountId = event.account().getNonNullId();
    String storeName = event.businessDetail().getName();
    LOGGER.info("[매장 등록 신청 승인 알림 이벤트] accountId: {}, storeName: {}", accountId, storeName);

    Account account = accountFinder.findOrThrow(accountId);

    TemplateEmail templateEmail = new TemplateEmail(
        STORE_REGISTRATION_APPROVE,
        new Email(account.getEmail().address()),
        "[모두의 웨이터] 매장 등록 신청이 승인되었습니다."
    );
    templateEmail.addTemplateVariable("name", storeName);
    templateEmail.addTemplateVariable("storeRegistrationUrl", STORE_REGISTRATION);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(RegistrationRejectEvent event) {
    LOGGER.info("[매장 등록 신청 반려 이벤트] storeName: {}", event.storeName());

    Account account = accountFinder.findOrThrow(event.accountId());

    TemplateEmail templateEmail = new TemplateEmail(
        STORE_REGISTRATION_REJECT,
        new Email(account.getEmail().address()),
        "[모두의 웨이터] 매장 등록 신청이 반려되었습니다."
    );
    templateEmail.addTemplateVariable("name", event.storeName());
    templateEmail.addTemplateVariable("reason", event.rejectReason());
    templateEmail.addTemplateVariable("storeRegistrationUrl", STORE_REGISTRATION);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

}
