package com.everyonewaiter.application.account;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.everyonewaiter.application.account.provided.AccountUpdater;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.store.RegistrationApproveEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class AccountAuthorizeEventHandler {

  private static final Logger LOGGER = getLogger(AccountAuthorizeEventHandler.class);

  private final AccountUpdater accountUpdater;

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void handle(RegistrationApproveEvent event) {
    Long accountId = event.account().getId();
    String storeName = event.businessDetail().getName();

    LOGGER.info("[사장님 권한 부여 이벤트] accountId: {}, storeName: {}", accountId, storeName);

    accountUpdater.authorize(accountId, AccountPermission.OWNER);
  }

}
