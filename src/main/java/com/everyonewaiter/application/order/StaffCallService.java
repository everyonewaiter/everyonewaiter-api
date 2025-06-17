package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.response.OrderResponse;
import com.everyonewaiter.domain.order.entity.StaffCall;
import com.everyonewaiter.domain.order.repository.StaffCallRepository;
import com.everyonewaiter.domain.order.service.StaffCallValidator;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffCallService {

  private final StoreRepository storeRepository;
  private final StaffCallValidator staffCallValidator;
  private final StaffCallRepository staffCallRepository;

  @Transactional
  public Long callStaff(Long storeId, int tableNo, String staffCallOptionName) {
    staffCallValidator.validateExistsStaffCallOption(storeId, staffCallOptionName);

    Store store = storeRepository.findByIdOrThrow(storeId);
    StaffCall staffCall = StaffCall.create(store, tableNo, staffCallOptionName);

    return staffCallRepository.save(staffCall).getId();
  }

  public OrderResponse.StaffCallDetails readAllIncomplete(Long storeId) {
    List<StaffCall> staffCalls = staffCallRepository.findAllIncompleteByStoreId(storeId);
    return OrderResponse.StaffCallDetails.from(staffCalls);
  }

}
