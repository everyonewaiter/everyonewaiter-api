package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.request.CategoryWrite;
import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.domain.menu.service.CategoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryValidator categoryValidator;
  private final CategoryRepository categoryRepository;

  @Transactional
  public Long create(Long storeId, CategoryWrite.Create request) {
    categoryValidator.validateUnique(storeId, request.name());

    int lastPosition = categoryRepository.findLastPositionByStoreId(storeId);
    Category category = Category.create(storeId, request.name(), lastPosition);

    return categoryRepository.save(category).getId();
  }

}
