package com.everyonewaiter.infrastructure.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface MenuJpaRepository extends JpaRepository<Menu, Long> {

  Long countByCategoryId(Long categoryId);

  List<Menu> findAllByCategoryId(Long categoryId);

  Optional<Menu> findByIdAndStoreId(Long menuId, Long storeId);

  Optional<Menu> findByIdAndStoreIdAndCategoryId(Long menuId, Long storeId, Long categoryId);

}
