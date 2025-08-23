package com.everyonewaiter.application.menu.provided;

import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuView;
import java.util.List;

public interface MenuFinder {

  List<Menu> findAll(Long storeId, Long categoryId);

  List<Menu> findAll(Long storeId, List<Long> menuIds);

  Menu findOrThrow(Long menuId, Long storeId);

  Menu findOrThrow(Long categoryId, Long menuId, Long storeId);

  MenuView.MenuDetail findViewOrThrow(Long menuId, Long storeId, Long categoryId);

}
