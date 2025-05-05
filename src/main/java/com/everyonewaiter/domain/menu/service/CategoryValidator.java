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

  public void validateUnique(Long storeId, String name) {
    if (categoryRepository.existsByStoreIdAndName(storeId, name)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_CATEGORY_NAME);
    }
  }

}
