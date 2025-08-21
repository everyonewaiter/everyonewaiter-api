package com.everyonewaiter.adapter.persistence.order;

import static com.everyonewaiter.domain.order.QOrder.order;
import static com.everyonewaiter.domain.order.QOrderMenu.orderMenu;
import static com.everyonewaiter.domain.pos.QPosTable.posTable;
import static com.everyonewaiter.domain.pos.QPosTableActivity.posTableActivity;
import static com.everyonewaiter.domain.store.QStore.store;

import com.everyonewaiter.application.order.required.OrderRepository;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OrderRepositoryImpl implements OrderRepository {

  private final JPAQueryFactory queryFactory;
  private final OrderJpaRepository orderJpaRepository;

  @Override
  public List<Order> findAll(Long storeId, boolean served) {
    return queryFactory
        .select(order)
        .from(order)
        .innerJoin(order.store, store)
        .innerJoin(order.posTableActivity, posTableActivity).fetchJoin()
        .innerJoin(order.posTableActivity.posTable, posTable).fetchJoin()
        .leftJoin(order.orderMenus, orderMenu).fetchJoin()
        .where(
            order.store.id.eq(storeId),
            order.serving.served.eq(served),
            order.createdAt.gt(store.lastOpenedAt)
        )
        .fetch();
  }

  @Override
  public List<Order> findAllActive(Long storeId, int tableNo) {
    return queryFactory
        .select(order)
        .from(order)
        .innerJoin(order.posTableActivity, posTableActivity).fetchJoin()
        .innerJoin(order.posTableActivity.posTable, posTable).fetchJoin()
        .leftJoin(order.orderMenus, orderMenu).fetchJoin()
        .where(
            order.store.id.eq(storeId),
            posTable.tableNo.eq(tableNo),
            posTable.active.isTrue(),
            posTableActivity.active.isTrue()
        )
        .fetch();
  }

  @Override
  public Order findOrThrow(Long orderId) {
    return orderJpaRepository.findById(orderId)
        .orElseThrow(OrderNotFoundException::new);
  }

  @Override
  public Order findOrThrow(Long orderId, Long storeId) {
    return orderJpaRepository.findByIdAndStoreId(orderId, storeId)
        .orElseThrow(OrderNotFoundException::new);
  }

  @Override
  public Order save(Order order) {
    return orderJpaRepository.save(order);
  }

}
