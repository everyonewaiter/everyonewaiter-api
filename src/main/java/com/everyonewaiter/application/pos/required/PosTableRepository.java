package com.everyonewaiter.application.pos.required;

import com.everyonewaiter.domain.pos.PosTable;
import java.util.List;

public interface PosTableRepository {

  List<PosTable> findAllActive(Long storeId);

  PosTable findActiveOrThrow(Long storeId, int tableNo);

  PosTable save(PosTable posTable);

  void saveAll(List<PosTable> posTables);

  void close(Long storeId);

}
