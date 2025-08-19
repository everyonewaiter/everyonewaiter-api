package com.everyonewaiter.application.staffcall.provided;

import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallCreateRequest;
import jakarta.validation.Valid;
import java.util.List;

public interface StaffCallManager {

  StaffCall create(Long storeId, int tableNo, @Valid StaffCallCreateRequest createRequest);

  StaffCall complete(Long storeId, Long staffCallId);

  List<StaffCall> findAllIncompleted(Long storeId);

}
