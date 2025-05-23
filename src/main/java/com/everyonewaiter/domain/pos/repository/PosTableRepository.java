package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.pos.entity.PosTable;
import java.util.List;

public interface PosTableRepository {

  void close(Long storeId);

  void saveAll(List<PosTable> tables);

  List<PosTable> findAllActiveByStoreId(Long storeId);

}
