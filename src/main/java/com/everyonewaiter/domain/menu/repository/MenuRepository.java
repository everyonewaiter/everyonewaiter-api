package com.everyonewaiter.domain.menu.repository;

import com.everyonewaiter.domain.menu.entity.Menu;

public interface MenuRepository {

  Long countByCategoryId(Long categoryId);

  int findLastPositionByCategoryId(Long categoryId);

  Menu save(Menu menu);

}
