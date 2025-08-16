package com.everyonewaiter.adapter.persistence.store;

import com.everyonewaiter.domain.store.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface StoreJpaRepository extends JpaRepository<Store, Long> {

  boolean existsByIdAndAccountId(Long id, Long accountId);

  @EntityGraph(attributePaths = "setting")
  Optional<Store> findByIdAndAccountId(Long id, Long accountId);

}
