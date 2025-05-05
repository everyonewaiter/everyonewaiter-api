package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Category;

public interface CategoryRepository {

  boolean existsByStoreIdAndName(Long storeId, String name);

  int findLastPositionByStoreId(Long storeId);

  Category save(Category category);

}
