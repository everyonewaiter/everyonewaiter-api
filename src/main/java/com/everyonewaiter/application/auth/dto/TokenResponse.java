package com.everyonewaiter.application.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TokenResponse")
public record TokenResponse(
    @Schema(description = "액세스 토큰 (12시간)", example = "abcdefghijklmnopqrstuvwxyz")
    String accessToken,

    @Schema(description = "리프레시 토큰 (2주)", example = "abcdefghijklmnopqrstuvwxyz")
    String refreshToken
) {

}
