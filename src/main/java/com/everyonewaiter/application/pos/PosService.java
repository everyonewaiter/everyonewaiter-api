package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PosService {

  private final PosTableRepository posTableRepository;

  @Transactional(readOnly = true)
  public Optional<PosResponse.TableActivityDetail> readActiveTable(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    return posTable.getActiveActivity().map(PosResponse.TableActivityDetail::from);
  }

  @Transactional(readOnly = true)
  public PosResponse.Tables readAllActiveTables(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);
    return PosResponse.Tables.from(posTables);
  }

}
