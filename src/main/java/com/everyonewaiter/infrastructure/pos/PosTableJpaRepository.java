package com.everyonewaiter.infrastructure.pos;

import com.everyonewaiter.domain.pos.entity.PosTable;
import org.springframework.data.jpa.repository.JpaRepository;

interface PosTableJpaRepository extends JpaRepository<PosTable, Long> {

}
