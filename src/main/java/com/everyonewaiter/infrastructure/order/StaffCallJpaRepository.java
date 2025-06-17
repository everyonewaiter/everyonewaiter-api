package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.StaffCall;
import org.springframework.data.jpa.repository.JpaRepository;

interface StaffCallJpaRepository extends JpaRepository<StaffCall, Long> {

}
