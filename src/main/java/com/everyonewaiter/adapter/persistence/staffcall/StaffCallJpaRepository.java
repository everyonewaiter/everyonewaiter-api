package com.everyonewaiter.adapter.persistence.staffcall;

import com.everyonewaiter.domain.staffcall.StaffCall;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface StaffCallJpaRepository extends JpaRepository<StaffCall, Long> {

  Optional<StaffCall> findByIdAndStoreId(Long staffCallId, Long storeId);

}
