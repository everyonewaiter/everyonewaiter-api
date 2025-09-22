package com.everyonewaiter.application.staffcall.provided;

import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallRequest;
import jakarta.validation.Valid;
import java.util.List;

public interface StaffCallManager {

  StaffCall call(Long storeId, int tableNo, @Valid StaffCallRequest callRequest);

  StaffCall complete(Long storeId, Long staffCallId);

  List<StaffCall> findAllIncompleted(Long storeId);

}
