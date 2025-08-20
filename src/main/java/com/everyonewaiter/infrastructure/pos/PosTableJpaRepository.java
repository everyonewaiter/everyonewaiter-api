package com.everyonewaiter.infrastructure.pos;

import com.everyonewaiter.domain.pos.PosTable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface PosTableJpaRepository extends JpaRepository<PosTable, Long> {

  Optional<PosTable> findByStoreIdAndTableNoAndActive(Long storeId, int tableNo, boolean active);

  List<PosTable> findAllByStoreIdAndActive(Long storeId, boolean active);

}
