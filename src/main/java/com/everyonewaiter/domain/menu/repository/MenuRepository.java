package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Menu;
import java.util.List;

public interface MenuRepository {

  Long countByCategoryId(Long categoryId);

  int findLastPositionByCategoryId(Long categoryId);

  List<Menu> findAllByCategoryId(Long categoryId);

  Menu save(Menu menu);

  void deleteAllByCategoryId(Long categoryId);

}
