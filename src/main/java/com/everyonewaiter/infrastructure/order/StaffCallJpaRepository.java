package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.StaffCall;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface StaffCallJpaRepository extends JpaRepository<StaffCall, Long> {

  boolean existsByStoreIdAndState(Long storeId, StaffCall.State state);

  List<StaffCall> findAllByStoreIdAndState(Long storeId, StaffCall.State state);

}
