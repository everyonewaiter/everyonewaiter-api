package com.everyonewaiter.domain.menu.service;

import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

  private final CategoryRepository categoryRepository;

  public void validateExceedMaxCount(Long storeId) {
    Long categoryCount = categoryRepository.countByStoreId(storeId);
    if (categoryCount >= 30) {
      throw new BusinessException(ErrorCode.EXCEED_MAXIMUM_CATEGORY_COUNT);
    }
  }

  public void validateUnique(Long storeId, String name) {
    if (categoryRepository.existsByStoreIdAndName(storeId, name)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_CATEGORY_NAME);
    }
  }

  public void validateUniqueExcludeId(Long categoryId, Long storeId, String name) {
    if (categoryRepository.existsByStoreIdAndNameExcludeId(categoryId, storeId, name)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_CATEGORY_NAME);
    }
  }

}
