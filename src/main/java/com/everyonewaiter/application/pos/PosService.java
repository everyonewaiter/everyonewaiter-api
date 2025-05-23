package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PosService {

  private final PosTableRepository posTableRepository;

  public PosResponse.Tables readAllActiveTables(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);
    return PosResponse.Tables.from(posTables);
  }

}
