package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

interface StoreJpaRepository extends JpaRepository<Store, Long> {

}
