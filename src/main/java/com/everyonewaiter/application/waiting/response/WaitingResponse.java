package com.everyonewaiter.application.waiting.response;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingResponse {

  @Schema(name = "WaitingResponse.RegistrationCount")
  public record RegistrationCount(
      @Schema(description = "현재 매장에 대기중인 팀 수", example = "5")
      int count
  ) {

    public static RegistrationCount from(int count) {
      return new RegistrationCount(count);
    }

  }

  @Schema(name = "WaitingResponse.MyTurn")
  public record MyTurn(
      @Schema(description = "대기 번호", example = "1")
      int number,

      @Schema(description = "웨이팅 등록 당시 내 앞 대기팀 수", example = "0")
      int initWaitingTeamCount,

      @Schema(description = "현재 내 앞 대기팀 수", example = "0")
      int currentWaitingTeamCount,

      @Schema(description = "상태", example = "REGISTRATION")
      Waiting.State state
  ) {

    public static MyTurn of(Waiting waiting, int currentWaitingTeamCount) {
      return new MyTurn(
          waiting.getNumber(),
          waiting.getInitWaitingTeamCount(),
          currentWaitingTeamCount,
          waiting.getState()
      );
    }

  }

  @Schema(name = "WaitingResponse.Details")
  public record Details(List<Detail> waitings) {

    public static Details from(List<Waiting> waitings) {
      return new Details(
          waitings.stream()
              .map(Detail::from)
              .toList()
      );
    }

  }

  @Schema(name = "WaitingResponse.Detail")
  public record Detail(
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
      Waiting.State state,

      @Schema(description = "웨이팅 등록일", example = "2025-01-01 12:00:00")
      Instant createdAt
  ) {

    public static Detail from(Waiting waiting) {
      return new Detail(
          Objects.requireNonNull(waiting.getId()).toString(),
          waiting.getPhoneNumber(),
          waiting.getAdult(),
          waiting.getInfant(),
          waiting.getNumber(),
          waiting.getCustomerCallCount().getValue(),
          waiting.getCustomerCallCount().getLastCallTime(),
          waiting.getState(),
          waiting.getCreatedAt()
      );
    }

  }

}
