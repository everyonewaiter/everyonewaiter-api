package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.request.CategoryWrite;
import com.everyonewaiter.application.menu.response.CategoryResponse;
import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.domain.menu.service.CategoryValidator;
import java.util.List;
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
    categoryValidator.validateExceedMaxCount(storeId);
    categoryValidator.validateUnique(storeId, request.name());

    int lastPosition = categoryRepository.findLastPositionByStoreId(storeId);
    Category category = Category.create(storeId, request.name(), lastPosition);

    return categoryRepository.save(category).getId();
  }

  @Transactional
  public void update(Long categoryId, Long storeId, CategoryWrite.Update request) {
    categoryValidator.validateUniqueExcludeId(categoryId, storeId, request.name());

    Category category = categoryRepository.findByIdAndStoreIdOrThrow(categoryId, storeId);
    category.update(request.name());

    categoryRepository.save(category);
  }

  @Transactional
  public void movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      CategoryWrite.MovePosition request
  ) {
    Category source = categoryRepository.findByIdAndStoreIdOrThrow(sourceId, storeId);
    Category target = categoryRepository.findByIdAndStoreIdOrThrow(targetId, storeId);

    boolean isMoved = source.movePosition(target, request.where());
    if (isMoved) {
      categoryRepository.shiftPosition(source);
      categoryRepository.save(source);
    }
  }

  @Transactional
  public void delete(Long categoryId, Long storeId) {
    Category category = categoryRepository.findByIdAndStoreIdOrThrow(categoryId, storeId);
    categoryRepository.delete(category);
  }

  public CategoryResponse.Simples readAll(Long storeId) {
    List<Category> categories = categoryRepository.findAll(storeId);
    return CategoryResponse.Simples.from(categories);
  }

}
