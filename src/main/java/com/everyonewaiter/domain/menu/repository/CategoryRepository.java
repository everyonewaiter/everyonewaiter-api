package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Category;

public interface CategoryRepository {

  boolean existsByStoreIdAndName(Long storeId, String name);

  boolean existsByStoreIdAndNameExcludeId(Long categoryId, Long storeId, String name);

  int findLastPositionByStoreId(Long storeId);

  Category findByIdAndStoreIdOrThrow(Long categoryId, Long storeId);

  Category save(Category category);

}
