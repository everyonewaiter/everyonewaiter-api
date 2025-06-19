package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.global.annotation.RedissonLock;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PosService {

  private final PosTableRepository posTableRepository;

  @Transactional
  @RedissonLock(key = {"#storeId + '-' + #sourceTableNo", "#storeId + '-' + #targetTableNo"})
  public void moveTable(Long storeId, int sourceTableNo, int targetTableNo) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);
    PosTable sourcePosTable = getPosTable(posTables, sourceTableNo);
    PosTable targetPosTable = getPosTable(posTables, targetTableNo);

    if (targetPosTable.hasActiveActivity()) {
      targetPosTable.merge(sourcePosTable);
    } else {
      sourcePosTable.move(targetPosTable);
    }

    // TODO: SSE 알림 이벤트 발행
  }

  private PosTable getPosTable(List<PosTable> posTables, int tableNo) {
    return posTables.stream()
        .filter(posTable -> posTable.getTableNo() == tableNo)
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_NOT_FOUND));
  }

  @Transactional
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void cancelOrder(Long storeId, int tableNo, Long orderId) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    posTable.cancelOrder(orderId);
    posTableRepository.save(posTable);
  }

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
