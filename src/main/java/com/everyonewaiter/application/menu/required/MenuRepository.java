package com.everyonewaiter.application.menu.required;

import com.everyonewaiter.domain.menu.Menu;
import java.util.List;

public interface MenuRepository {

  Long count(Long categoryId);

  int findLastPosition(Long categoryId);

  List<Menu> findAll(Long storeId, Long categoryId);

  List<Menu> findAll(Long storeId, List<Long> menuIds);

  Menu findOrThrow(Long menuId, Long storeId);

  Menu findOrThrow(Long menuId, Long storeId, Long categoryId);

  Menu save(Menu menu);

  void shiftPosition(Menu source);

  void delete(Menu menu);

  void deleteAll(List<Menu> menus);

}
