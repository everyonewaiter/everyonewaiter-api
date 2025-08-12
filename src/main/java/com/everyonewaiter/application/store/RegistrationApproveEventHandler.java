package com.everyonewaiter.application.store;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.store.entity.BusinessLicense;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.event.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class RegistrationApproveEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RegistrationApproveEventHandler.class);

  private final AccountRepository accountRepository;
  private final StoreRepository storeRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void consumeAuthorizeAccount(RegistrationApproveEvent event) {
    Long accountId = event.accountId();
    String storeName = event.businessLicense().getName();
    LOGGER.info("[계정 사장님 권한 부여 이벤트] accountId: {}, storeName: {}", accountId, storeName);

    Account account = accountRepository.findByIdOrThrow(accountId);
    account.authorize(Account.Permission.OWNER);
    accountRepository.save(account);
  }

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void consumeCreateStore(RegistrationApproveEvent event) {
    Long accountId = event.accountId();
    BusinessLicense businessLicense = event.businessLicense();
    LOGGER.info("[매장 생성 이벤트] accountId: {}, storeName: {}", accountId, businessLicense.getName());

    Store store = Store.create(accountId, businessLicense);
    storeRepository.save(store);
  }

}
