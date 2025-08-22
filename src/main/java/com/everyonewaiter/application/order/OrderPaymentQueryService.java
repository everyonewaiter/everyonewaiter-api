package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.provided.OrderPaymentFinder;
import com.everyonewaiter.application.order.required.OrderPaymentRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.order.OrderPayment;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class OrderPaymentQueryService implements OrderPaymentFinder {

  private final OrderPaymentRepository orderPaymentRepository;

  @Override
  public List<OrderPayment> findAll(Long storeId, Instant start, Instant end) {
    return orderPaymentRepository.findAll(storeId, start, end);
  }

  @Override
  public OrderPayment findOrThrow(Long orderPaymentId, Long storeId) {
    return orderPaymentRepository.findOrThrow(orderPaymentId, storeId);
  }

}
