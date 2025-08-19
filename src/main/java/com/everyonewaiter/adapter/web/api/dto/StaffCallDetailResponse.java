package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "StaffCallDetailResponse")
public record StaffCallDetailResponse(
    @Schema(description = "직원 호출 ID", example = "\"694865267482835533\"")
    String staffCallId,

    @Schema(description = "테이블 번호", example = "1")
    int tableNo,

    @Schema(description = "직원 호출 옵션명", example = "직원 호출")
    String name,

    @Schema(description = "직원 호출 상태", example = "INCOMPLETE")
    StaffCallState state,

    @Schema(description = "직원 호출 완료 시간", example = "1970-01-01 00:00:00")
    Instant completeTime,

    @Schema(description = "직원 호출 시간", example = "2025-01-01 12:00:00")
    Instant createdAt
) {

  public static StaffCallDetailResponse from(StaffCall staffCall) {
    return new StaffCallDetailResponse(
        staffCall.getNonNullId().toString(),
        staffCall.getTableNo(),
        staffCall.getName(),
        staffCall.getState(),
        staffCall.getCompleteTime(),
        staffCall.getCreatedAt()
    );
  }

}
