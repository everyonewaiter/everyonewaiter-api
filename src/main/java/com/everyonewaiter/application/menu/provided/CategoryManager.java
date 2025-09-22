package com.everyonewaiter.application.menu.provided;

import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryCreateRequest;
import com.everyonewaiter.domain.menu.CategoryMovePositionRequest;
import com.everyonewaiter.domain.menu.CategoryUpdateRequest;
import jakarta.validation.Valid;

public interface CategoryManager {

  Category create(Long storeId, @Valid CategoryCreateRequest createRequest);

  Category update(Long categoryId, Long storeId, @Valid CategoryUpdateRequest updateRequest);

  Category movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      @Valid CategoryMovePositionRequest movePositionRequest
  );

  void delete(Long categoryId, Long storeId);

}
