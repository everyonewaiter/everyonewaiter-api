package com.everyonewaiter.application.menu.response;

import com.everyonewaiter.domain.menu.entity.Menu;
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

}
