package com.everyonewaiter.adapter.persistence.store;

import com.everyonewaiter.domain.store.Registration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

  Optional<Registration> findByIdAndAccountId(Long id, Long accountId);

}
