package com.everyonewaiter.application.staffcall.required;

import com.everyonewaiter.domain.staffcall.StaffCall;
import java.util.List;

public interface StaffCallRepository {

  List<StaffCall> findAllIncompleted(Long storeId);

  StaffCall findOrThrow(Long staffCallId, Long storeId);

  StaffCall save(StaffCall staffCall);

}
