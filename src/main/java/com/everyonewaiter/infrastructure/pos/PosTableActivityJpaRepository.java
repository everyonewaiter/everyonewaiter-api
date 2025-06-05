package com.everyonewaiter.infrastructure.pos;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import org.springframework.data.jpa.repository.JpaRepository;

interface PosTableActivityJpaRepository extends JpaRepository<PosTableActivity, Long> {

}
