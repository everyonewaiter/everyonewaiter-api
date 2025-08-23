package com.everyonewaiter.application.store.required;

import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationPageRequest;

public interface RegistrationRepository {

  Paging<Registration> findAll(Long accountId, RegistrationPageRequest pageRequest);

  Paging<Registration> findAll(RegistrationAdminPageRequest pageRequest);

  Registration findOrThrow(Long registrationId);

  Registration findOrThrow(Long registrationId, Long accountId);

  Registration findWithAccountOrThrow(Long registrationId);

  Registration save(Registration registration);

}
