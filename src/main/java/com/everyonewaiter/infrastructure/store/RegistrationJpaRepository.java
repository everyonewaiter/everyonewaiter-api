package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Registration;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

  @Override
  @NonNull
  @EntityGraph(attributePaths = "businessLicense")
  Optional<Registration> findById(@NonNull Long id);

  @EntityGraph(attributePaths = "businessLicense")
  Optional<Registration> findByIdAndAccountId(Long id, Long accountId);

}
