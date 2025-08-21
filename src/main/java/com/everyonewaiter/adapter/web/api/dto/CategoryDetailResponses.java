package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.menu.CategoryView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "CategoryDetailResponses")
public record CategoryDetailResponses(List<CategoryView.CategoryDetail> categories) {

  public static CategoryDetailResponses from(List<CategoryView.CategoryDetail> categories) {
    return new CategoryDetailResponses(categories);
  }

}
