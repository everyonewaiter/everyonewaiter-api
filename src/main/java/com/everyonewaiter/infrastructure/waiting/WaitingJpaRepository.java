package com.everyonewaiter.infrastructure.waiting;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

  boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state);

  Optional<Waiting> findByIdAndStoreId(Long id, Long storeId);

}
