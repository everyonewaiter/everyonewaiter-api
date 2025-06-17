package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderJpaRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByIdAndStoreId(Long id, Long storeId);

}
