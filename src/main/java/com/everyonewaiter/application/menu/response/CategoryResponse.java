package com.everyonewaiter.application.menu.response;

import com.everyonewaiter.domain.menu.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponse {

  @Schema(name = "CategoryResponse.Simples")
  public record Simples(List<Simple> categories) {

    public static Simples from(List<Category> categories) {
      return new Simples(
          categories.stream()
              .map(Simple::from)
              .toList()
      );
    }

  }

  @Schema(name = "CategoryResponse.Simple")
  public record Simple(
      @Schema(description = "카테고리 ID", example = "\"694865267482835533\"")
      String categoryId,

      @Schema(description = "카테고리 이름", example = "스테이크")
      String name
  ) {

    public static Simple from(Category category) {
      return new Simple(Objects.requireNonNull(category.getId()).toString(), category.getName());
    }

  }

}
