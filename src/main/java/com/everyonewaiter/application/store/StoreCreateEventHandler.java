package com.everyonewaiter.application.store;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.store.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.Store;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class StoreCreateEventHandler {

  private static final Logger LOGGER = getLogger(StoreCreateEventHandler.class);

  private final StoreRepository storeRepository;

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void handle(RegistrationApproveEvent event) {
    LOGGER.info("[매장 생성 이벤트] accountId: {}, storeName: {}",
        event.account().getId(), event.businessDetail().getName());

    Store store = Store.create(event.account(), event.businessDetail());

    storeRepository.save(store);
  }

}
