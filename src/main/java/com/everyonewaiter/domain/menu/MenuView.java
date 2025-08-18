package com.everyonewaiter.domain.menu;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MenuView {

  @Schema(name = "MenuView.MenuDetail")
  public record MenuDetail(
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
      String image,

      @Schema(description = "메뉴 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 그룹 목록")
      List<MenuOptionGroupDetail> menuOptionGroups
  ) {

    public static MenuDetail from(Menu menu) {
      return new MenuDetail(
          menu.getNonNullId().toString(),
          menu.getCategory().getNonNullId().toString(),
          menu.getName(),
          menu.getDescription(),
          menu.getPrice(),
          menu.getSpicy(),
          menu.getState(),
          menu.getLabel(),
          menu.getImage(),
          menu.isPrintEnabled(),
          menu.getMenuOptionGroups()
              .stream()
              .map(MenuOptionGroupDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "MenuView.MenuOptionGroupDetail")
  public record MenuOptionGroupDetail(
      @Schema(description = "메뉴 옵션 그룹 ID", example = "\"694865267482835533\"")
      String menuOptionGroupId,

      @Schema(description = "메뉴 옵션 그룹명", example = "굽기 정도")
      String name,

      @Schema(description = "메뉴 옵션 그룹 타입 (필수, 옵셔널)", example = "MANDATORY")
      MenuOptionGroupType type,

      @Schema(description = "메뉴 옵션 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 목록")
      List<MenuOptionDetail> menuOptions
  ) {

    public static MenuOptionGroupDetail from(MenuOptionGroup menuOptionGroup) {
      return new MenuOptionGroupDetail(
          menuOptionGroup.getNonNullId().toString(),
          menuOptionGroup.getName(),
          menuOptionGroup.getType(),
          menuOptionGroup.isPrintEnabled(),
          menuOptionGroup.getMenuOptions()
              .stream()
              .map(MenuOptionDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "MenuView.MenuOptionDetail")
  public record MenuOptionDetail(
      @Schema(description = "메뉴 옵션명", example = "미디움")
      String name,

      @Schema(description = "메뉴 옵션 가격", example = "0")
      long price
  ) {

    public static MenuOptionDetail from(MenuOption menuOption) {
      return new MenuOptionDetail(
          menuOption.getName(),
          menuOption.getPrice()
      );
    }

  }

}
