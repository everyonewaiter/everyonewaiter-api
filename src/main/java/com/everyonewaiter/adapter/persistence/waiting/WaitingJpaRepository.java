package com.everyonewaiter.adapter.persistence.waiting;

import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingState;
import org.springframework.data.jpa.repository.JpaRepository;

interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

  boolean existsByPhoneNumberAndState(PhoneNumber phoneNumber, WaitingState state);

  boolean existsByStoreIdAndState(Long storeId, WaitingState state);

}
