package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffCallValidator {

  private final StoreRepository storeRepository;

  public void validateExistsStaffCallOption(Long storeId, String staffCallOptionName) {
    Store store = storeRepository.findByIdOrThrow(storeId);
    if (!store.hasStaffCallOption(staffCallOptionName)) {
      throw new BusinessException(ErrorCode.STAFF_CALL_OPTION_NOT_FOUND);
    }
  }

}
