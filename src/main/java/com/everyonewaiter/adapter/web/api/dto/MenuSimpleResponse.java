package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuLabel;
import com.everyonewaiter.domain.menu.MenuState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MenuSimpleResponse")
public record MenuSimpleResponse(
    @Schema(description = "메뉴 ID", example = "\"694865267482835533\"")
    String menuId,

    @Schema(description = "카테고리 ID", example = "\"694865267482835533\"")
    String categoryId,

    @Schema(description = "메뉴 이름", example = "안심 스테이크")
    String name,

    @Schema(description = "메뉴 설명", example = "1++ 한우 안심을 사용합니다.")
    String description,

    @Schema(description = "메뉴 가격", example = "34900")
    long price,

    @Schema(description = "메뉴 맵기 단계", example = "0")
    int spicy,

    @Schema(description = "메뉴 상태", example = "DEFAULT")
    MenuState state,

    @Schema(description = "메뉴 라벨", example = "BEST")
    MenuLabel label,

    @Schema(description = "메뉴 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
    String image
) {

  public static MenuSimpleResponse from(Menu menu) {
    return new MenuSimpleResponse(
        menu.getNonNullId().toString(),
        menu.getCategory().getNonNullId().toString(),
        menu.getName(),
        menu.getDescription(),
        menu.getPrice(),
        menu.getSpicy(),
        menu.getState(),
        menu.getLabel(),
        menu.getImage()
    );
  }

}
