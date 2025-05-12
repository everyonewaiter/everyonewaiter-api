package com.everyonewaiter.domain.menu.service;

import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
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

  public void validateOptionPrice(long menuPrice, MenuOptionGroup menuOptionGroup) {
    menuOptionGroup.getMenuOptions()
        .stream()
        .filter(menuOption -> menuOption.getPrice() < 0)
        .forEach(menuOption -> {
          if (Math.abs(menuOption.getPrice()) > menuPrice) {
            throw new BusinessException(ErrorCode.INVALID_DISCOUNT_OPTION_PRICE);
          }
        });
  }

}
