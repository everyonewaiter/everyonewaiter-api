package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.StaffCall;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface StaffCallJpaRepository extends JpaRepository<StaffCall, Long> {

  Optional<StaffCall> findByIdAndStoreId(Long id, Long storeId);

}
