package com.everyonewaiter.domain.waiting;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "WaitingMyTurnView")
public record WaitingMyTurnView(
    @Schema(description = "대기 번호", example = "1")
    int number,

    @Schema(description = "웨이팅 등록 당시 내 앞 대기팀 수", example = "0")
    int initWaitingTeamCount,

    @Schema(description = "현재 내 앞 대기팀 수", example = "0")
    int currentWaitingTeamCount,

    @Schema(description = "상태", example = "REGISTRATION")
    WaitingState state,

    @Schema(description = "웨이팅 등록일", example = "2025-01-01 12:00:00")
    Instant createdAt
) {

  public static WaitingMyTurnView of(Waiting waiting, int currentWaitingTeamCount) {
    return new WaitingMyTurnView(
        waiting.getNumber(),
        waiting.getInitWaitingTeamCount(),
        currentWaitingTeamCount,
        waiting.getState(),
        waiting.getCreatedAt()
    );
  }

}
