package com.everyonewaiter.application.receipt;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.receipt.required.ReceiptPrintNoRepository;
import com.everyonewaiter.domain.store.StoreCloseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class PrintNoDeleteEventHandler {

  private static final Logger LOGGER = getLogger(PrintNoDeleteEventHandler.class);

  private final ReceiptPrintNoRepository receiptRepository;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(StoreCloseEvent event) {
    LOGGER.info("[빌지 번호 삭제 이벤트] 매장 ID: {}", event.storeId());

    receiptRepository.delete(event.storeId());
  }

}
