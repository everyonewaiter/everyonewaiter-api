package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.provided.MenuFinder;
import com.everyonewaiter.application.menu.required.MenuRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class MenuQueryService implements MenuFinder {

  private final MenuRepository menuRepository;

  @Override
  @ReadOnlyTransactional
  public List<Menu> findAll(Long storeId, Long categoryId) {
    return menuRepository.findAll(storeId, categoryId);
  }

  @Override
  @ReadOnlyTransactional
  public List<Menu> findAll(Long storeId, List<Long> menuIds) {
    return menuRepository.findAll(storeId, menuIds);
  }

  @Override
  @ReadOnlyTransactional
  public Menu findOrThrow(Long menuId, Long storeId) {
    return menuRepository.findOrThrow(menuId, storeId);
  }

  @Override
  @ReadOnlyTransactional
  public Menu findOrThrow(Long categoryId, Long menuId, Long storeId) {
    return menuRepository.findOrThrow(menuId, storeId, categoryId);
  }

  @Override
  @Transactional(readOnly = true)
  public MenuView.MenuDetail findViewOrThrow(Long menuId, Long storeId, Long categoryId) {
    Menu menu = menuRepository.findOrThrow(menuId, storeId, categoryId);

    return MenuView.MenuDetail.from(menu);
  }

}
