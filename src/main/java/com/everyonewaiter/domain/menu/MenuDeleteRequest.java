package com.everyonewaiter.domain.menu;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "MenuDeleteRequest")
public record MenuDeleteRequest(
    @Schema(description = "삭제할 메뉴 ID 목록", requiredMode = REQUIRED)
    @NotNull(message = "삭제할 메뉴 ID 목록이 누락되었습니다.")
    @Size(min = 1, message = "삭제할 메뉴 ID 목록을 1개 이상 입력해 주세요.")
    List<Long> menuIds
) {

}
