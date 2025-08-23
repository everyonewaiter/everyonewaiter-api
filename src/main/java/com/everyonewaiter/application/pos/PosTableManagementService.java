package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableManager;
import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableDiscountRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class PosTableManagementService implements PosTableManager {

  private final PosTableRepository posTableRepository;

  @Override
  @DistributedLock(key = {"#storeId + '-' + #sourceTableNo", "#storeId + '-' + #targetTableNo"})
  public Optional<PosTable> moveTable(Long storeId, int sourceTableNo, int targetTableNo) {
    if (sourceTableNo == targetTableNo) {
      return Optional.empty();
    }

    PosTable source = posTableRepository.findActiveOrThrow(storeId, sourceTableNo);
    PosTable target = posTableRepository.findActiveOrThrow(storeId, targetTableNo);

    if (target.hasActiveActivity()) {
      target.merge(source);

      return Optional.ofNullable(posTableRepository.save(target));
    } else {
      source.move(target);

      return Optional.ofNullable(posTableRepository.save(source));
    }
  }

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public PosTable discount(Long storeId, int tableNo, PosTableDiscountRequest discountRequest) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    posTable.discount(discountRequest);

    return posTableRepository.save(posTable);
  }

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public PosTable completeActivity(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    posTable.completeActiveActivity();

    return posTableRepository.save(posTable);
  }

}
