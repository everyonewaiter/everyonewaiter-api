package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.staffcall.StaffCall;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "StaffCallDetailResponses")
public record StaffCallDetailResponses(List<StaffCallDetailResponse> staffCalls) {

  public static StaffCallDetailResponses from(List<StaffCall> staffCalls) {
    return new StaffCallDetailResponses(
        staffCalls.stream()
            .map(StaffCallDetailResponse::from)
            .toList()
    );
  }

}
