package com.everyonewaiter.adapter.persistence.order;

import static com.everyonewaiter.domain.order.QOrderPayment.orderPayment;

import com.everyonewaiter.application.order.required.OrderPaymentRepository;
import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentNotFoundException;
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
  public List<OrderPayment> findAll(Long storeId, Instant start, Instant end) {
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
  public OrderPayment findOrThrow(Long orderPaymentId, Long storeId) {
    return orderPaymentJpaRepository.findByIdAndStoreId(orderPaymentId, storeId)
        .orElseThrow(OrderPaymentNotFoundException::new);
  }

  @Override
  public OrderPayment save(OrderPayment orderPayment) {
    return orderPaymentJpaRepository.save(orderPayment);
  }

}
