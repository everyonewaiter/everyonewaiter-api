package com.everyonewaiter.application.order;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.order.provided.OrderFinder;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderCreateEvent;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.service.ReceiptFactory;
import com.everyonewaiter.domain.sse.ServerAction;
import com.everyonewaiter.domain.sse.SseCategory;
import com.everyonewaiter.domain.sse.SseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class OrderCreateEventHandler {

  private static final Logger LOGGER = getLogger(OrderCreateEventHandler.class);

  private final OrderFinder orderFinder;
  private final ReceiptFactory receiptFactory;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Async("eventTaskExecutor")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  public void consume(OrderCreateEvent event) {
    LOGGER.info("[주문 생성 이벤트] 주문 ID: {}", event.orderId());

    Order order = orderFinder.findOrThrow(event.orderId());
    Receipt receipt = receiptFactory.createReceipt(order);

    applicationEventPublisher.publishEvent(
        new SseEvent(event.storeId(), SseCategory.RECEIPT, ServerAction.CREATE, receipt)
    );
  }

}
