package com.everyonewaiter.infrastructure.account;

import com.everyonewaiter.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

interface AccountJpaRepository extends JpaRepository<Account, Long> {

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

}
