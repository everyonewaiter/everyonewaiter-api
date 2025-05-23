package com.everyonewaiter.domain.store.service;

import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreValidator {

  private final StoreRepository storeRepository;

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

}
