package com.everyonewaiter.application.store.provided;

import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationRejectRequest;
import jakarta.validation.Valid;

public interface RegistrationAdministrator {

  Registration approve(Long registrationId);

  Registration reject(Long registrationId, @Valid RegistrationRejectRequest rejectRequest);

}
