package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.StaffCall;
import java.util.List;

public interface StaffCallRepository {

  boolean existsIncompleteByStoreId(Long storeId);

  List<StaffCall> findAllIncompleteByStoreId(Long storeId);

  StaffCall save(StaffCall staffCall);

}
