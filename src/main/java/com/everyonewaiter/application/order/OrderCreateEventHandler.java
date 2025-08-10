package com.everyonewaiter.application.order;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.event.OrderCreateEvent;
import com.everyonewaiter.domain.order.repository.OrderRepository;
import com.everyonewaiter.domain.order.service.ReceiptFactory;
import com.everyonewaiter.domain.sse.ServerAction;
import com.everyonewaiter.domain.sse.SseCategory;
import com.everyonewaiter.domain.sse.SseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class OrderCreateEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateEventHandler.class);

  private final OrderRepository orderRepository;
  private final ReceiptFactory receiptFactory;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Async("eventTaskExecutor")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  public void consume(OrderCreateEvent event) {
    LOGGER.info("[주문 생성 이벤트] 주문 ID: {}", event.orderId());

    Order order = orderRepository.findByIdOrThrow(event.orderId());
    Receipt receipt = receiptFactory.createReceipt(order);

    applicationEventPublisher.publishEvent(
        new SseEvent(event.storeId(), SseCategory.RECEIPT, ServerAction.CREATE, receipt)
    );
  }

}
