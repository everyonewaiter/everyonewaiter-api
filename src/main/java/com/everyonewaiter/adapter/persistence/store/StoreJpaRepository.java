package com.everyonewaiter.adapter.persistence.store;

import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface StoreJpaRepository extends JpaRepository<Store, Long> {

  boolean existsByIdAndAccountId(Long id, Long accountId);

  boolean existsByIdAndStatus(Long id, StoreStatus status);

  @EntityGraph(attributePaths = "setting")
  Optional<Store> findByIdAndAccountId(Long id, Long accountId);

}
