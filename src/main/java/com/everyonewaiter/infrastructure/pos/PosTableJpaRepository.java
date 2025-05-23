package com.everyonewaiter.infrastructure.pos;

import com.everyonewaiter.domain.pos.entity.PosTable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface PosTableJpaRepository extends JpaRepository<PosTable, Long> {

  List<PosTable> findAllByStoreIdAndState(Long storeId, PosTable.State state);

}
