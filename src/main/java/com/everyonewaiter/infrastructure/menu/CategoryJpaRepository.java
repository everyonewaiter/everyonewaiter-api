package com.everyonewaiter.infrastructure.menu;

import com.everyonewaiter.domain.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryJpaRepository extends JpaRepository<Category, Long> {

  boolean existsByStoreIdAndName(Long storeId, String name);

}
