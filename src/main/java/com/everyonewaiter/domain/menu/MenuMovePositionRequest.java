package com.everyonewaiter.domain.menu;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.everyonewaiter.domain.shared.PositionMove;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "MenuMovePositionRequest")
public record MenuMovePositionRequest(
    @Schema(description = "대상 메뉴로 이동할 위치(전,후)", example = "NEXT", requiredMode = REQUIRED)
    @NotNull(message = "이동할 위치가 누락되었습니다.")
    PositionMove where
) {

}
