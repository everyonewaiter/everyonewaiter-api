package com.everyonewaiter.domain.menu;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CategoryView {

  @Schema(name = "CategoryDetails")
  public record CategoryDetails(List<CategoryView.CategoryDetail> categories) {

    public static CategoryDetails from(List<CategoryView.CategoryDetail> categories) {
      return new CategoryDetails(categories);
    }

  }

  @Schema(name = "CategoryView.CategoryDetail")
  public record CategoryDetail(
      @Schema(description = "카테고리 ID", example = "\"694865267482835533\"")
      String categoryId,

      @Schema(description = "카테고리 이름", example = "스테이크")
      String name,

      @Schema(description = "메뉴 목록")
      List<MenuView.MenuDetail> menus
  ) {

    public static CategoryDetail from(Category category) {
      return new CategoryDetail(
          String.valueOf(category.getId()),
          category.getName(),
          category.getMenus()
              .stream()
              .map(MenuView.MenuDetail::from)
              .toList()
      );
    }

  }

}
