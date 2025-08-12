package com.everyonewaiter.adapter.persistence.account;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface AccountJpaRepository extends JpaRepository<Account, Long> {

  boolean existsByEmail(Email email);

  boolean existsByEmailAndState(Email email, Account.State state);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  Optional<Account> findByEmail(Email email);

  Optional<Account> findByPhoneNumber(PhoneNumber phoneNumber);

}
