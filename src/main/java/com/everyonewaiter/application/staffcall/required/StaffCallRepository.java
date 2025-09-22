package com.everyonewaiter.application.staffcall.required;

import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallState;
import java.util.List;

public interface StaffCallRepository {

  List<StaffCall> findAll(Long storeId, StaffCallState state);

  StaffCall findOrThrow(Long staffCallId, Long storeId);

  StaffCall save(StaffCall staffCall);

}
