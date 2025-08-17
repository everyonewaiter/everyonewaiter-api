package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.provided.CategoryManager;
import com.everyonewaiter.application.menu.required.CategoryRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.support.CacheName;
import com.everyonewaiter.domain.menu.AlreadyUseCategoryNameException;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryCreateRequest;
import com.everyonewaiter.domain.menu.CategoryMovePositionRequest;
import com.everyonewaiter.domain.menu.CategoryUpdateRequest;
import com.everyonewaiter.domain.menu.ExceedMaxCategoryCountException;
import com.everyonewaiter.domain.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class CategoryModifyService implements CategoryManager {

  private final StoreFinder storeFinder;
  private final CategoryRepository categoryRepository;

  @Override
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Category create(Long storeId, CategoryCreateRequest createRequest) {
    validateCategoryCreate(storeId, createRequest);

    Store store = storeFinder.findOrThrow(storeId);
    int lastPosition = categoryRepository.findLastPosition(storeId);

    Category category = Category.create(store, createRequest, lastPosition);

    return categoryRepository.save(category);
  }

  private void validateCategoryCreate(Long storeId, CategoryCreateRequest createRequest) {
    Long categoryCount = categoryRepository.count(storeId);
    if (categoryCount >= 30) {
      throw new ExceedMaxCategoryCountException();
    }

    if (categoryRepository.exists(storeId, createRequest.name())) {
      throw new AlreadyUseCategoryNameException();
    }
  }

  @Override
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Category update(Long categoryId, Long storeId, CategoryUpdateRequest updateRequest) {
    validateCategoryUpdate(categoryId, storeId, updateRequest);

    Category category = categoryRepository.findByIdAndStoreIdOrThrow(categoryId, storeId);

    category.update(updateRequest);

    return categoryRepository.save(category);
  }

  private void validateCategoryUpdate(
      Long categoryId,
      Long storeId,
      CategoryUpdateRequest updateRequest
  ) {
    if (categoryRepository.existsExcludeId(categoryId, storeId, updateRequest.name())) {
      throw new AlreadyUseCategoryNameException();
    }
  }

  @Override
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Category movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      CategoryMovePositionRequest movePositionRequest
  ) {
    Category source = categoryRepository.findByIdAndStoreIdOrThrow(sourceId, storeId);
    Category target = categoryRepository.findByIdAndStoreIdOrThrow(targetId, storeId);

    boolean isMoved = source.movePosition(target, movePositionRequest);
    if (isMoved) {
      categoryRepository.shiftPosition(source);
    }

    return categoryRepository.save(source);
  }

  @Override
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void delete(Long categoryId, Long storeId) {
    Category category = categoryRepository.findByIdAndStoreIdOrThrow(categoryId, storeId);

    category.delete();

    categoryRepository.delete(category);
  }

}
