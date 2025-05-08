package com.everyonewaiter.infrastructure.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

interface MenuJpaRepository extends JpaRepository<Menu, Long> {

  Long countByCategoryId(Long categoryId);

}
