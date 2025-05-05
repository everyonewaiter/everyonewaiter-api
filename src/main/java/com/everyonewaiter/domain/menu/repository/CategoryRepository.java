package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Category;
import java.util.List;

public interface CategoryRepository {

  Long countByStoreId(Long storeId);

  boolean existsByStoreIdAndName(Long storeId, String name);

  boolean existsByStoreIdAndNameExcludeId(Long categoryId, Long storeId, String name);

  List<Category> findAll(Long storeId);

  int findLastPositionByStoreId(Long storeId);

  Category findByIdAndStoreIdOrThrow(Long categoryId, Long storeId);

  Category save(Category category);

  void shiftPosition(Category source);

  void delete(Category category);

}
