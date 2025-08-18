package com.everyonewaiter.domain.menu;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "MenuCreateRequest")
public record MenuCreateRequest(
    @Schema(description = "메뉴 이름", example = "알리오올리오", requiredMode = REQUIRED)
    @NotBlank(message = "메뉴 이름을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "메뉴 이름은 1자 이상 30자 이하로 입력해 주세요.")
    String name,

    @Schema(description = "메뉴 설명", example = "오일 파스타 대표 메뉴", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 설명 정보가 누락되었습니다.")
    @Size(max = 100, message = "메뉴 설명은 100자 이하로 입력해 주세요.")
    String description,

    @Schema(description = "메뉴 가격", example = "19900", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 가격 정보가 누락되었습니다.")
    @Min(value = 0, message = "메뉴 가격은 0 이상으로 입력해 주세요.")
    @Max(value = 10_000_000, message = "메뉴 가격은 10,000,000 이하로 입력해 주세요.")
    long price,

    @Schema(description = "맵기 단계", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 맵기 단계 정보가 누락되었습니다.")
    @Min(value = 0, message = "메뉴 맵기 단계는 0 이상으로 입력해 주세요.")
    @Max(value = 10, message = "메뉴 맵기 단게는 10 이하로 입력해 주세요.")
    int spicy,

    @Schema(description = "메뉴 상태", example = "DEFAULT", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 상태가 누락되었거나 올바르지 않습니다.")
    MenuState state,

    @Schema(description = "메뉴 라벨", example = "BEST", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 라벨이 누락되었거나 올바르지 않습니다.")
    MenuLabel label,

    @Schema(description = "주방 프린트 출력 여부", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 주방 프린트 출력 여부 정보가 누락되었습니다.")
    boolean printEnabled,

    @Schema(description = "메뉴 옵션 그룹", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 옵션 그룹 정보가 누락되었습니다.")
    @Size(max = 20, message = "메뉴 옵션 그룹은 메뉴당 최대 20개까지 등록할 수 있어요.")
    List<@Valid MenuOptionGroupModifyRequest> menuOptionGroups
) {

}
