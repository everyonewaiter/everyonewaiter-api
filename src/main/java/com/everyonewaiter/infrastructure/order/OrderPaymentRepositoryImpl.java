package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.order.repository.OrderPaymentRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OrderPaymentRepositoryImpl implements OrderPaymentRepository {

  private final OrderPaymentJpaRepository orderPaymentJpaRepository;

  @Override
  public OrderPayment findByIdAndStoreId(Long orderPaymentId, Long storeId) {
    return orderPaymentJpaRepository.findByIdAndStoreId(orderPaymentId, storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_PAYMENT_NOT_FOUND));
  }

  @Override
  public OrderPayment save(OrderPayment orderPayment) {
    return orderPaymentJpaRepository.save(orderPayment);
  }

}
