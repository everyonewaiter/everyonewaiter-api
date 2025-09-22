package com.everyonewaiter.adapter.persistence.pos;

import com.everyonewaiter.domain.pos.PosTableActivity;
import org.springframework.data.jpa.repository.JpaRepository;

interface PosTableActivityJpaRepository extends JpaRepository<PosTableActivity, Long> {

}
