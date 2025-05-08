package com.everyonewaiter.domain.menu.service;

import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuValidator {

  private final MenuRepository menuRepository;

  public void validateExceedMaxCount(Long categoryId) {
    Long menuCount = menuRepository.countByCategoryId(categoryId);
    if (menuCount >= 50) {
      throw new BusinessException(ErrorCode.EXCEED_MAXIMUM_MENU_COUNT);
    }
  }

}
