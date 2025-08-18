package com.everyonewaiter.adapter.persistence.menu;

import com.everyonewaiter.domain.menu.Menu;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface MenuJpaRepository extends JpaRepository<Menu, Long> {

  Long countByCategoryId(Long categoryId);

  @EntityGraph(attributePaths = "menuOptionGroups")
  Optional<Menu> findByIdAndStoreId(Long menuId, Long storeId);

  @EntityGraph(attributePaths = "menuOptionGroups")
  Optional<Menu> findByIdAndStoreIdAndCategoryId(Long menuId, Long storeId, Long categoryId);

}
