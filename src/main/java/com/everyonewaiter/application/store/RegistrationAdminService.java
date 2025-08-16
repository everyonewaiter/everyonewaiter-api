package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.provided.RegistrationAdministrator;
import com.everyonewaiter.application.store.provided.RegistrationFinder;
import com.everyonewaiter.application.store.required.RegistrationRepository;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationRejectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class RegistrationAdminService implements RegistrationAdministrator {

  private final RegistrationFinder registrationFinder;
  private final RegistrationRepository registrationRepository;

  @Override
  public Registration approve(Long registrationId) {
    Registration registration = registrationFinder.findOrThrow(registrationId);

    registration.approve();

    return registrationRepository.save(registration);
  }

  @Override
  public Registration reject(Long registrationId, RegistrationRejectRequest rejectRequest) {
    Registration registration = registrationFinder.findOrThrow(registrationId);

    registration.reject(rejectRequest);

    return registrationRepository.save(registration);
  }

}
