package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.menu.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategorySimpleResponse")
public record CategorySimpleResponse(
    @Schema(description = "카테고리 ID", example = "\"694865267482835533\"")
    String categoryId,

    @Schema(description = "카테고리 이름", example = "스테이크")
    String name
) {

  public static CategorySimpleResponse from(Category category) {
    return new CategorySimpleResponse(String.valueOf(category.getId()), category.getName());
  }

}
