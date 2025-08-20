package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.pos.PosTable;
import java.util.List;

public interface PosTableRepository {

  List<PosTable> findAllActive(Long storeId);

  PosTable findActiveOrThrow(Long storeId, int tableNo);

  void close(Long storeId);

  PosTable save(PosTable posTable);

  void saveAll(List<PosTable> posTables);

}
