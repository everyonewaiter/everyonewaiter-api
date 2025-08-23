package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "WaitingDetailResponse")
public record WaitingDetailResponse(
    @Schema(description = "웨이팅 ID", example = "\"694865267482835533\"")
    String waitingId,

    @Schema(description = "휴대폰 번호", example = "01044591812")
    String phoneNumber,

    @Schema(description = "성인 인원 수", example = "2")
    int adult,

    @Schema(description = "유아 인원 수", example = "0")
    int infant,

    @Schema(description = "대기 번호", example = "1")
    int number,

    @Schema(description = "총 호출 횟수", example = "0")
    int callCount,

    @Schema(description = "마지막 호출 시간", example = "1970-01-01 00:00:00")
    Instant lastCallTime,

    @Schema(description = "상태", example = "REGISTRATION")
    WaitingState state,

    @Schema(description = "웨이팅 등록일", example = "2025-01-01 12:00:00")
    Instant createdAt
) {

  public static WaitingDetailResponse from(Waiting waiting) {
    return new WaitingDetailResponse(
        String.valueOf(waiting.getId()),
        waiting.getPhoneNumber().value(),
        waiting.getAdult(),
        waiting.getInfant(),
        waiting.getNumber(),
        waiting.getCustomerCall().getCount(),
        waiting.getCustomerCall().getLastCallTime(),
        waiting.getState(),
        waiting.getCreatedAt()
    );
  }

}
