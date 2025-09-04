package com.everyonewaiter.application.staffcall;

import com.everyonewaiter.application.staffcall.provided.StaffCallManager;
import com.everyonewaiter.application.staffcall.required.StaffCallRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallOptionNotFoundException;
import com.everyonewaiter.domain.staffcall.StaffCallRequest;
import com.everyonewaiter.domain.staffcall.StaffCallState;
import com.everyonewaiter.domain.store.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class StaffCallManagementService implements StaffCallManager {

  private final StoreFinder storeFinder;
  private final StaffCallRepository staffCallRepository;

  @Override
  @Transactional
  public StaffCall call(Long storeId, int tableNo, StaffCallRequest callRequest) {
    Store store = storeFinder.findOrThrow(storeId);

    validateStaffCall(store, callRequest);

    StaffCall staffCall = StaffCall.call(store, tableNo, callRequest);

    return staffCallRepository.save(staffCall);
  }

  private void validateStaffCall(Store store, StaffCallRequest callRequest) {
    if (!store.hasStaffCallOption(callRequest.optionName())) {
      throw new StaffCallOptionNotFoundException();
    }
  }

  @Override
  @Transactional
  public StaffCall complete(Long storeId, Long staffCallId) {
    StaffCall staffCall = staffCallRepository.findOrThrow(staffCallId, storeId);

    staffCall.complete();

    return staffCallRepository.save(staffCall);
  }

  @Override
  @ReadOnlyTransactional
  public List<StaffCall> findAllIncompleted(Long storeId) {
    return staffCallRepository.findAll(storeId, StaffCallState.INCOMPLETE);
  }

}
