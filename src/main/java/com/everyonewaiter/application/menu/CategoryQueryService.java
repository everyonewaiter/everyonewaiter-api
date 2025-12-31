package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.provided.CategoryFinder;
import com.everyonewaiter.application.menu.required.CategoryRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CategoryQueryService implements CategoryFinder {

  private final CategoryRepository categoryRepository;

  @Override
  @ReadOnlyTransactional
  public List<Category> findAll(Long storeId) {
    return categoryRepository.findAll(storeId);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryView.CategoryDetails findAllView(Long storeId) {
    return CategoryView.CategoryDetails.from(
        categoryRepository.findAll(storeId).stream()
            .map(CategoryView.CategoryDetail::from)
            .toList()
    );
  }

  @Override
  @ReadOnlyTransactional
  public Category findOrThrow(Long categoryId, Long storeId) {
    return categoryRepository.findOrThrow(categoryId, storeId);
  }

}
