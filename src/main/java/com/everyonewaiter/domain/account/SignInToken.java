package com.everyonewaiter.domain.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SignInToken")
public record SignInToken(
    @Schema(description = "액세스 토큰 (3시간)", example = "abcdefghijklmnopqrstuvwxyz")
    String accessToken
) {

}
