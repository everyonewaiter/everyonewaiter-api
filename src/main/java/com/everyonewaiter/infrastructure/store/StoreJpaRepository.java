package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface StoreJpaRepository extends JpaRepository<Store, Long> {

  @EntityGraph(attributePaths = "setting")
  Optional<Store> findByIdAndAccountId(Long id, Long accountId);

}
