package com.everyonewaiter.infrastructure.order;

import static com.everyonewaiter.domain.order.entity.QOrderPayment.orderPayment;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.order.repository.OrderPaymentRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OrderPaymentRepositoryImpl implements OrderPaymentRepository {

  private final JPAQueryFactory queryFactory;
  private final OrderPaymentJpaRepository orderPaymentJpaRepository;

  @Override
  public List<OrderPayment> findAllByStoreIdAndDate(Long storeId, Instant start, Instant end) {
    return queryFactory
        .select(orderPayment)
        .from(orderPayment)
        .where(
            orderPayment.store.id.eq(storeId),
            orderPayment.createdAt.goe(start),
            orderPayment.createdAt.loe(end)
        )
        .orderBy(orderPayment.id.desc())
        .fetch();
  }

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
