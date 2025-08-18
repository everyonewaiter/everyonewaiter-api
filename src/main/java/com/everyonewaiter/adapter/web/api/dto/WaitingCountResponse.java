package com.everyonewaiter.adapter.web.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "WaitingCountResponse")
public record WaitingCountResponse(
    @Schema(description = "현재 매장에 대기중인 팀 수", example = "5")
    int count
) {

  public static WaitingCountResponse from(int count) {
    return new WaitingCountResponse(count);
  }

}
