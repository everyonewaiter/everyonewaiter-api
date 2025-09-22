package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.menu.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "CategorySimpleResponses")
public record CategorySimpleResponses(List<CategorySimpleResponse> categories) {

  public static CategorySimpleResponses from(List<Category> categories) {
    return new CategorySimpleResponses(
        categories.stream()
            .map(CategorySimpleResponse::from)
            .toList()
    );
  }

}
