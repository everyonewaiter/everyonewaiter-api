package com.everyonewaiter.domain.menu;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "MenuOptionGroupModifyRequest")
public record MenuOptionGroupModifyRequest(
    @Schema(description = "메뉴 옵션 그룹명", example = "필수 옵션", requiredMode = REQUIRED)
    @NotBlank(message = "메뉴 옵션 그룹명을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "메뉴 이름은 1자 이상 30자 이하로 입력해 주세요.")
    String name,

    @Schema(description = "메뉴 옵션 그룹 타입 (필수, 옵셔널)", example = "MANDATORY", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 옵션 그룹 타입이 누락되었거나 올바르지 않습니다.")
    MenuOptionGroupType type,

    @Schema(description = "주방 프린트 출력 여부", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 주방 프린트 출력 여부 정보가 누락되었습니다.")
    boolean printEnabled,

    @Schema(description = "메뉴 옵션", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 옵션 정보가 누락되었습니다.")
    @Size(min = 1, max = 20, message = "메뉴 옵션을 1개 이상 20개 이하로 등록해 주세요.")
    List<@Valid MenuOptionModifyRequest> menuOptions
) {

}
