package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.order.QOrder.order;
import static com.everyonewaiter.domain.order.QOrderPayment.orderPayment;
import static com.everyonewaiter.domain.pos.QPosTable.posTable;
import static com.everyonewaiter.domain.pos.QPosTableActivity.posTableActivity;
import static java.util.Objects.requireNonNullElse;

import com.everyonewaiter.domain.order.OrderPaymentMethod;
import com.everyonewaiter.domain.order.OrderPaymentState;
import com.everyonewaiter.domain.pos.PosTableActiveActivityNotFoundException;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.PosTableActivityNotFoundException;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.view.PosTableActivityView;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PosTableActivityRepositoryImpl implements PosTableActivityRepository {

  private final JPAQueryFactory queryFactory;
  private final PosTableActivityJpaRepository posTableActivityJpaRepository;

  @Override
  public PosTableActivityView.TotalRevenue getTotalRevenue(
      Long storeId,
      Instant start,
      Instant end
  ) {
    return queryFactory
        .select(
            Projections.constructor(
                PosTableActivityView.TotalRevenue.class,
                order.price.sumLong(),
                posTableActivity.discount.sumLong(),
                orderPayment.amount.sumLong()
            )
        )
        .from(posTableActivity)
        .leftJoin(posTableActivity.orders, order)
        .leftJoin(posTableActivity.payments, orderPayment)
        .where(
            posTableActivity.store.id.eq(storeId),
            posTableActivity.active.isFalse(),
            posTableActivity.createdAt.goe(start),
            posTableActivity.createdAt.loe(end)
        )
        .fetchFirst();
  }

  @Override
  public long getPaymentRevenue(
      Long storeId,
      Instant start,
      Instant end,
      OrderPaymentMethod method,
      OrderPaymentState state
  ) {
    Long revenue = queryFactory
        .select(orderPayment.amount.sumLong())
        .from(posTableActivity)
        .leftJoin(posTableActivity.payments, orderPayment)
        .on(
            orderPayment.method.eq(method),
            orderPayment.state.eq(state)
        )
        .where(
            posTableActivity.store.id.eq(storeId),
            posTableActivity.active.isFalse(),
            posTableActivity.createdAt.goe(start),
            posTableActivity.createdAt.loe(end)
        )
        .fetchFirst();

    return requireNonNullElse(revenue, 0L);
  }

  @Override
  public Optional<PosTableActivity> findActive(Long storeId, int tableNo) {
    return Optional.ofNullable(
        queryFactory
            .select(posTableActivity)
            .from(posTableActivity)
            .where(
                posTableActivity.store.id.eq(storeId),
                posTableActivity.active.isTrue(),
                posTableActivity.tableNo.eq(tableNo)
            )
            .fetchFirst()
    );
  }

  @Override
  public PosTableActivity findActiveOrThrow(Long storeId, int tableNo) {
    return findActive(storeId, tableNo)
        .orElseThrow(PosTableActiveActivityNotFoundException::new);
  }

  @Override
  public PosTableActivity findOrThrow(Long posTableActivityId, Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(posTableActivity)
                .from(posTableActivity)
                .innerJoin(posTableActivity.posTable, posTable).fetchJoin()
                .where(
                    posTableActivity.id.eq(posTableActivityId),
                    posTableActivity.store.id.eq(storeId)
                )
                .fetchFirst()
        )
        .orElseThrow(PosTableActivityNotFoundException::new);
  }

  @Override
  public PosTableActivity save(PosTableActivity posTableActivity) {
    return posTableActivityJpaRepository.save(posTableActivity);
  }

}
