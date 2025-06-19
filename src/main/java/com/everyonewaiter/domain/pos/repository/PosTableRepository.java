package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.pos.entity.PosTable;
import java.util.List;

public interface PosTableRepository {

  void close(Long storeId);

  PosTable save(PosTable posTable);

  void saveAll(List<PosTable> posTables);

  PosTable findActiveByStoreIdAndTableNo(Long storeId, int tableNo);

  List<PosTable> findAllActiveByStoreId(Long storeId);

}
