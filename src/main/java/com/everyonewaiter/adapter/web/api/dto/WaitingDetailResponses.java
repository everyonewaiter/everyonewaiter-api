package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.waiting.Waiting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "WaitingDetailResponses")
public record WaitingDetailResponses(List<WaitingDetailResponse> waitings) {

  public static WaitingDetailResponses from(List<Waiting> waitings) {
    return new WaitingDetailResponses(
        waitings.stream()
            .map(WaitingDetailResponse::from)
            .toList()
    );
  }

}
