package com.everyonewaiter.application.store;

import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.notification.service.MailSender;
import com.everyonewaiter.domain.notification.service.request.MailSend;
import com.everyonewaiter.domain.store.event.RegistrationRejectEvent;
import com.everyonewaiter.global.config.ClientUrlRegistry;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class RegistrationRejectEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RegistrationRejectEventHandler.class);

  private final ClientUrlRegistry clientUrlRegistry;
  private final MailSender mailSender;
  private final AccountRepository accountRepository;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(RegistrationRejectEvent event) {
    LOGGER.info("[매장 등록 신청 반려 이벤트] storeName: {}", event.storeName());

    accountRepository.findById(event.accountId())
        .ifPresent(account -> {
          String registrationUrl = clientUrlRegistry.getUrls().getFirst() + "/stores/registrations";
          mailSender.sendTo(MailSend.of(
              account.getEmail(),
              "store-registration-reject",
              "[모두의 웨이터] 매장 등록 신청이 반려되었습니다.",
              Map.of(
                  "name", event.storeName(),
                  "reason", event.rejectReason(),
                  "storeRegistrationUrl", registrationUrl
              )
          ));
        });
  }

}
