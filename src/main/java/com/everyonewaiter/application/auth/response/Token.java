package com.everyonewaiter.application.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

  @Schema(name = "Token.AllResponse")
  public record AllResponse(
      @Schema(description = "액세스 토큰 (12시간)", example = "abcdefghijklmnopqrstuvwxyz")
      String accessToken,

      @Schema(description = "리프레시 토큰 (2주)", example = "abcdefghijklmnopqrstuvwxyz")
      String refreshToken
  ) {

  }

}
