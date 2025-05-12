package com.everyonewaiter.infrastructure.menu;

import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface MenuOptionGroupJpaRepository extends JpaRepository<MenuOptionGroup, Long> {

  @Modifying
  @Query(value = "delete from menu_option_group where menu_id = :menuId", nativeQuery = true)
  void deleteAllByMenuId(Long menuId);

}
