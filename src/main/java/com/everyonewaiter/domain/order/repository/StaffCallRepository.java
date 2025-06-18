package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.StaffCall;
import java.util.List;

public interface StaffCallRepository {

  List<StaffCall> findAllIncompleteByStoreId(Long storeId);

  StaffCall findByIdAndStoreIdOrThrow(Long staffCallId, Long storeId);

  StaffCall save(StaffCall staffCall);

}
