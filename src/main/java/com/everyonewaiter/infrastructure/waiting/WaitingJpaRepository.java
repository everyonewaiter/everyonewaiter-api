package com.everyonewaiter.infrastructure.waiting;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

  boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state);

  boolean existsByStoreIdAndState(Long storeId, Waiting.State state);

}
