package com.everyonewaiter.application.waiting.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingResponse {

  @Schema(name = "WaitingResponse.TotalCount")
  public record RegistrationCount(int count) {

    public static RegistrationCount from(int count) {
      return new RegistrationCount(count);
    }

  }

}
