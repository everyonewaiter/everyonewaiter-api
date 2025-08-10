package com.everyonewaiter.application.notification;

import static com.everyonewaiter.domain.notification.EmailTemplate.EMAIL_AUTHENTICATION;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_APPROVE;
import static com.everyonewaiter.domain.notification.EmailTemplate.STORE_REGISTRATION_REJECT;

import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.domain.notification.TemplateEmail;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.store.event.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.event.RegistrationRejectEvent;
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
class EmailNotificationEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationEventHandler.class);

  private final ClientUrlRegistry clientUrl;
  private final JwtProvider jwtProvider;
  private final AccountRepository accountRepository;
  private final NotificationSender notificationSender;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeAuthMailSendEvent(AuthMailSendEvent event) {
    LOGGER.info("[이메일 인증 메일 전송 이벤트] email: {}", event.email());

    JwtPayload payload = new JwtPayload(JwtFixedId.VERIFICATION_EMAIL, event.email());
    String authToken = jwtProvider.generate(payload, Duration.ofDays(1));
    String authUrl = clientUrl.getBaseUrl()
        + "/auth/mail?email="
        + event.email()
        + "&token="
        + authToken;

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(event.email()),
        "[모두의 웨이터] 이메일 인증 안내드립니다.",
        EMAIL_AUTHENTICATION
    );
    templateEmail.addTemplateVariable("authenticationUrl", authUrl);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationApproveEvent(RegistrationApproveEvent event) {
    Long accountId = event.accountId();
    String storeName = event.businessLicense().getName();
    LOGGER.info("[매장 등록 신청 승인 알림 이벤트] accountId: {}, storeName: {}", accountId, storeName);

    Account account = accountRepository.findByIdOrThrow(accountId);
    String registrationUrl = clientUrl.getBaseUrl() + "/stores/registrations";

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(account.getEmail()),
        "[모두의 웨이터] 매장 등록 신청이 승인되었습니다.",
        STORE_REGISTRATION_APPROVE
    );
    templateEmail.addTemplateVariable("name", storeName);
    templateEmail.addTemplateVariable("storeRegistrationUrl", registrationUrl);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consumeRegistrationRejectEvent(RegistrationRejectEvent event) {
    LOGGER.info("[매장 등록 신청 반려 이벤트] storeName: {}", event.storeName());

    Account account = accountRepository.findByIdOrThrow(event.accountId());
    String registrationUrl = clientUrl.getBaseUrl() + "/stores/registrations";

    TemplateEmail templateEmail = new TemplateEmail(
        new Email(account.getEmail()),
        "[모두의 웨이터] 매장 등록 신청이 반려되었습니다.",
        STORE_REGISTRATION_REJECT
    );
    templateEmail.addTemplateVariable("name", event.storeName());
    templateEmail.addTemplateVariable("reason", event.rejectReason());
    templateEmail.addTemplateVariable("storeRegistrationUrl", registrationUrl);

    notificationSender.sendEmailOneToOne(templateEmail);
  }

}
