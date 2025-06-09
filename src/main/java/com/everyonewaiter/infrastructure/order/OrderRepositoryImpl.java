package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class OrderRepositoryImpl implements OrderRepository {

  private final OrderJpaRepository orderJpaRepository;

  @Override
  public Order save(Order order) {
    return orderJpaRepository.save(order);
  }

}
