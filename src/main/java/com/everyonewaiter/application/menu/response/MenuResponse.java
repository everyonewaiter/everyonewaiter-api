package com.everyonewaiter.application.menu.response;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOption;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

  @Schema(name = "MenuResponse.Simples")
  public record Simples(List<Simple> menus) {

    public static Simples from(List<Menu> menus) {
      return new Simples(
          menus.stream()
              .map(Simple::from)
              .toList()
      );
    }

  }

  @Schema(name = "MenuResponse.Simple")
  public record Simple(
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
      Menu.State state,

      @Schema(description = "메뉴 라벨", example = "BEST")
      Menu.Label label,

      @Schema(description = "메뉴 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
      String image
  ) {

    public static Simple from(Menu menu) {
      return new Simple(
          Objects.requireNonNull(menu.getId()).toString(),
          Objects.requireNonNull(menu.getCategory().getId()).toString(),
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

  @Schema(name = "MenuResponse.Detail")
  public record Detail(
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
      Menu.State state,

      @Schema(description = "메뉴 라벨", example = "BEST")
      Menu.Label label,

      @Schema(description = "메뉴 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
      String image,

      @Schema(description = "메뉴 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 그룹 목록")
      List<OptionGroup> menuOptionGroups
  ) {

    public static Detail from(Menu menu) {
      return new Detail(
          Objects.requireNonNull(menu.getId()).toString(),
          Objects.requireNonNull(menu.getCategory().getId()).toString(),
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
              .map(OptionGroup::from)
              .toList()
      );
    }

  }

  @Schema(name = "MenuResponse.OptionGroup")
  public record OptionGroup(
      @Schema(description = "메뉴 옵션 그룹 ID", example = "\"694865267482835533\"")
      String menuOptionGroupId,

      @Schema(description = "메뉴 옵션 그룹명", example = "굽기 정도")
      String name,

      @Schema(description = "메뉴 옵션 그룹 타입 (필수, 옵셔널)", example = "MANDATORY")
      MenuOptionGroup.Type type,

      @Schema(description = "메뉴 옵션 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 목록")
      List<Option> menuOptions
  ) {

    public static OptionGroup from(MenuOptionGroup menuOptionGroup) {
      return new OptionGroup(
          Objects.requireNonNull(menuOptionGroup.getId()).toString(),
          menuOptionGroup.getName(),
          menuOptionGroup.getType(),
          menuOptionGroup.isPrintEnabled(),
          menuOptionGroup.getMenuOptions()
              .stream()
              .map(Option::from)
              .toList()
      );
    }

  }

  @Schema(name = "MenuResponse.Option")
  public record Option(
      @Schema(description = "메뉴 옵션명", example = "미디움")
      String name,

      @Schema(description = "메뉴 옵션 가격", example = "0")
      long price
  ) {

    public static Option from(MenuOption menuOption) {
      return new Option(
          menuOption.getName(),
          menuOption.getPrice()
      );
    }

  }

}
