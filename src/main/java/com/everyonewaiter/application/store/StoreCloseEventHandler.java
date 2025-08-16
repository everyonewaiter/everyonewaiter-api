package com.everyonewaiter.application.store;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.store.StoreCloseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class StoreCloseEventHandler {

  private static final Logger LOGGER = getLogger(StoreCloseEventHandler.class);

  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void handle(StoreCloseEvent event) {
    Long storeId = event.storeId();
    LOGGER.info("[매장 영업 종료 이벤트] storeId: {}", storeId);

    posTableRepository.close(storeId);
  }

}
