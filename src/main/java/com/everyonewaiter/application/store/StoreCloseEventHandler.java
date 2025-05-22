package com.everyonewaiter.application.store;

import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.store.event.StoreCloseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class StoreCloseEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(StoreCloseEventHandler.class);

  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void consume(StoreCloseEvent event) {
    Long storeId = event.storeId();
    LOGGER.info("[매장 영업 종료 이벤트] storeId: {}", storeId);

    posTableRepository.close(storeId);
  }

}
