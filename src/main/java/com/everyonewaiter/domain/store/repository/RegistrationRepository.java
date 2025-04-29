package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Registration;
import java.util.Optional;

public interface RegistrationRepository {

  Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId);

  Registration save(Registration registration);

}
