package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import java.util.List;

public interface MenuRepository {

  Long countByCategoryId(Long categoryId);

  int findLastPositionByCategoryId(Long categoryId);

  List<Menu> findAllByCategoryId(Long categoryId);

  List<Menu> findAllByStoreIdAndCategoryId(Long storeId, Long categoryId);

  List<Menu> findAllByStoreIdAndIds(Long storeId, List<Long> menuIds);

  Menu findByIdAndStoreIdOrThrow(Long menuId, Long storeId);

  Menu findByIdAndStoreIdAndCategoryIdOrThrow(Long menuId, Long storeId, Long categoryId);

  Menu save(Menu menu);

  void saveAllMenuOptionGroups(Long menuId, List<MenuOptionGroup> menuOptionGroups);

  void shiftPosition(Menu source);

  void delete(Menu menu);

  void deleteAll(List<Menu> menus);

  void deleteAllByCategoryId(Long categoryId);

}
