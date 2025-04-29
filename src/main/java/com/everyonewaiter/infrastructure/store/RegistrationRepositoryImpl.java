package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class RegistrationRepositoryImpl implements RegistrationRepository {

  private final RegistrationJpaRepository registrationJpaRepository;

  @Override
  public Registration save(Registration registration) {
    return registrationJpaRepository.save(registration);
  }

}
