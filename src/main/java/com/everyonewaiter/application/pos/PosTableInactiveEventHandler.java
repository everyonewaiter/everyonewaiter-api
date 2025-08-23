package com.everyonewaiter.application.pos;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.domain.store.StoreCloseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class PosTableInactiveEventHandler {

  private static final Logger LOGGER = getLogger(PosTableInactiveEventHandler.class);

  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void handle(StoreCloseEvent event) {
    LOGGER.info("[매장 영업 종료 이벤트] storeId: {}", event.storeId());

    posTableRepository.close(event.storeId());
  }

}
