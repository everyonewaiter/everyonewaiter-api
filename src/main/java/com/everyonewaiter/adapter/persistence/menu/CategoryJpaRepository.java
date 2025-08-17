package com.everyonewaiter.adapter.persistence.menu;

import com.everyonewaiter.domain.menu.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryJpaRepository extends JpaRepository<Category, Long> {

  Long countByStoreId(Long storeId);

  boolean existsByStoreIdAndName(Long storeId, String name);

  Optional<Category> findByIdAndStoreId(Long id, Long storeId);

}
