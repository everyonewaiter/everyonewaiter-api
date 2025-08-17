package com.everyonewaiter.application.menu.required;

import com.everyonewaiter.domain.menu.Category;
import java.util.List;

public interface CategoryRepository {

  Long count(Long storeId);

  boolean exists(Long storeId, String name);

  boolean existsExcludeId(Long categoryId, Long storeId, String name);

  int findLastPosition(Long storeId);

  List<Category> findAll(Long storeId);

  Category findByIdAndStoreIdOrThrow(Long categoryId, Long storeId);

  Category save(Category category);

  void shiftPosition(Category source);

  void delete(Category category);

}
