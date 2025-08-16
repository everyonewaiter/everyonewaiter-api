package com.everyonewaiter.application.store.required;

import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminDetailView;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationAdminPageView;
import com.everyonewaiter.domain.store.RegistrationPageRequest;

public interface RegistrationRepository {

  Registration findByIdOrThrow(Long registrationId);

  Registration findByIdAndAccountIdOrThrow(Long registrationId, Long accountId);

  Paging<Registration> findAllByAccountId(Long accountId, RegistrationPageRequest pageRequest);

  RegistrationAdminDetailView findByAdminOrThrow(Long registrationId);

  Paging<RegistrationAdminPageView> findAllByAdmin(RegistrationAdminPageRequest pageRequest);

  Registration save(Registration registration);

}
