package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.order.entity.QOrder.order;
import static com.everyonewaiter.domain.order.entity.QOrderPayment.orderPayment;
import static com.everyonewaiter.domain.pos.entity.QPosTable.posTable;
import static com.everyonewaiter.domain.pos.entity.QPosTableActivity.posTableActivity;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.view.PosTableActivityView;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.Objects;
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
      OrderPayment.Method method,
      OrderPayment.State state
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
    return Objects.requireNonNullElse(revenue, 0L);
  }

  @Override
  public PosTableActivity findByIdAndStoreIdOrThrow(Long posTableActivityId, Long storeId) {
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
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_ACTIVITY_NOT_FOUND));
  }

  @Override
  public Optional<PosTableActivity> findByStoreIdAndTableNo(Long storeId, int tableNo) {
    return Optional.ofNullable(
        queryFactory
            .select(posTableActivity)
            .from(posTableActivity)
            .innerJoin(posTableActivity.posTable, posTable).fetchJoin()
            .where(
                posTableActivity.store.id.eq(storeId),
                posTableActivity.active.isTrue(),
                posTableActivity.posTable.tableNo.eq(tableNo)
            )
            .fetchFirst()
    );
  }

  @Override
  public PosTableActivity findByStoreIdAndTableNoOrThrow(Long storeId, int tableNo) {
    return findByStoreIdAndTableNo(storeId, tableNo)
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND));
  }

  @Override
  public PosTableActivity save(PosTableActivity posTableActivity) {
    return posTableActivityJpaRepository.save(posTableActivity);
  }

}
