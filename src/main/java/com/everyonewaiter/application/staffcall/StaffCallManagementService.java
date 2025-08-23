package com.everyonewaiter.application.staffcall;

import com.everyonewaiter.application.staffcall.provided.StaffCallManager;
import com.everyonewaiter.application.staffcall.required.StaffCallRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallCreateRequest;
import com.everyonewaiter.domain.staffcall.StaffCallOptionNotFoundException;
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
  public StaffCall create(Long storeId, int tableNo, StaffCallCreateRequest createRequest) {
    Store store = storeFinder.findOrThrow(storeId);

    validateStaffCallCreate(store, createRequest);

    StaffCall staffCall = StaffCall.create(store, tableNo, createRequest);

    return staffCallRepository.save(staffCall);
  }

  private void validateStaffCallCreate(Store store, StaffCallCreateRequest createRequest) {
    if (!store.hasStaffCallOption(createRequest.optionName())) {
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
