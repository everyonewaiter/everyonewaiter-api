package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderPaymentJpaRepository extends JpaRepository<OrderPayment, Long> {

  Optional<OrderPayment> findByIdAndStoreId(Long id, Long storeId);

}
