package com.everyonewaiter.application.receipt;

import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.SseCategory.RECEIPT;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com.everyonewaiter.application.receipt.provided.ReceiptCreator;
import com.everyonewaiter.domain.order.OrderCancelEvent;
import com.everyonewaiter.domain.order.OrderCreateEvent;
import com.everyonewaiter.domain.order.OrderUpdateEvent;
import com.everyonewaiter.domain.receipt.Receipt;
import com.everyonewaiter.domain.receipt.ReceiptResendEvent;
import com.everyonewaiter.domain.sse.SseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class ReceiptSendEventHandler {

  private static final Logger LOGGER = getLogger(ReceiptSendEventHandler.class);

  private final ReceiptCreator receiptCreator;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Async("eventTaskExecutor")
  @Transactional(propagation = REQUIRES_NEW)
  @TransactionalEventListener
  public void handle(OrderCreateEvent event) {
    LOGGER.info("[주문 빌지 전송 이벤트] 매장 ID: {}, 주문 ID: {}, 테이블 번호: {}",
        event.storeId(), event.orderId(), event.tableNo());

    Receipt receipt = receiptCreator.create(event.storeId(), event.tableNo(), event.orderId());

    publishSseEvent(event.storeId(), receipt);
  }

  @Async("eventTaskExecutor")
  @Transactional(propagation = REQUIRES_NEW)
  @TransactionalEventListener
  public void handle(OrderUpdateEvent event) {
    LOGGER.info("[주문 수정 빌지 전송 이벤트] 매장 ID: {}, 테이블 번호: {}", event.storeId(), event.tableNo());

    publishSseEvent(event.storeId(), event.receipt());
  }

  @Async("eventTaskExecutor")
  @Transactional(propagation = REQUIRES_NEW)
  @TransactionalEventListener
  public void handle(OrderCancelEvent event) {
    LOGGER.info("[주문 취소 빌지 전송 이벤트] 매장 ID: {}, 주문 ID: {}, 테이블 번호: {}",
        event.storeId(), event.orderId(), event.tableNo());

    Receipt cancel = receiptCreator.createCancel(event.storeId(), event.tableNo(), event.orderId());

    publishSseEvent(event.storeId(), cancel);
  }

  @Async("eventTaskExecutor")
  @Transactional(propagation = REQUIRES_NEW)
  @TransactionalEventListener
  public void handle(ReceiptResendEvent event) {
    LOGGER.info("[주문 빌지 주방 재전송 이벤트] 매장 ID: {}, 주문 ID: {}", event.storeId(), event.orderIds());

    Receipt receipt = receiptCreator.create(event.storeId(), event.tableNo(), event.orderIds());

    publishSseEvent(event.storeId(), receipt);
  }

  private void publishSseEvent(Long storeId, Receipt receipt) {
    if (!receipt.receiptMenus().isEmpty()) {
      applicationEventPublisher.publishEvent(new SseEvent(storeId, RECEIPT, CREATE, receipt));
    }
  }

}
