package com.everyonewaiter.domain.store.service;

import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreValidator {

  private final StoreRepository storeRepository;
  private final PosTableRepository posTableRepository;
  private final WaitingRepository waitingRepository;

  public void validateExists(Long storeId) {
    if (!storeRepository.existsById(storeId)) {
      throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
    }
  }

  public void validateOwner(Long storeId, Long accountId) {
    if (!storeRepository.existsByIdAndAccountId(storeId, accountId)) {
      throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
    }
  }

  public void validateOpen(Long storeId) {
    if (storeRepository.findByIdOrThrow(storeId).isClosed()) {
      throw new BusinessException(ErrorCode.STORE_IS_CLOSED);
    }
  }

  public void validateClose(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);

    if (posTables.stream().anyMatch(PosTable::hasActiveActivity)) {
      throw new BusinessException(ErrorCode.INCOMPLETE_POS_TABLE_ACTIVITY);
    }
    if (waitingRepository.existsByStoreIdAndState(storeId, Waiting.State.REGISTRATION)) {
      throw new BusinessException(ErrorCode.INCOMPLETE_WAITING);
    }
  }

}
