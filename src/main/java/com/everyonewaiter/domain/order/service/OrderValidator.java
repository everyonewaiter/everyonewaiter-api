package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.application.menu.provided.MenuFinder;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

  private final MenuFinder menuFinder;

  public void validateCreateOrder(Long storeId, Set<Long> menuIds) {
    if (menuIds.isEmpty()) {
      throw new BusinessException(ErrorCode.NOT_EMPTY_ORDER_MENU);
    }

    List<Menu> menus = menuFinder.findAll(storeId, menuIds.stream().toList());
    if (menus.size() != menuIds.size()) {
      throw new BusinessException(ErrorCode.ORDER_MENU_NOT_FOUND);
    }

    if (!menus.stream().allMatch(Menu::canOrder)) {
      throw new BusinessException(ErrorCode.INCLUDE_SOLD_OUT_MENU);
    }
  }

}
