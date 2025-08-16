package com.everyonewaiter.application.store.provided;

import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminDetailView;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationAdminPageView;
import com.everyonewaiter.domain.store.RegistrationPageRequest;
import jakarta.validation.Valid;

public interface RegistrationFinder {

  Registration findOrThrow(Long registrationId);

  Registration findOrThrow(Long registrationId, Long accountId);

  Paging<Registration> findAll(Long accountId, @Valid RegistrationPageRequest pageRequest);

  RegistrationAdminDetailView findByAdminOrThrow(Long registrationId);

  Paging<RegistrationAdminPageView> findAllByAdmin(@Valid RegistrationAdminPageRequest pageRequest);

}
