package com.everyonewaiter.application.store.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationApplyRequest;
import com.everyonewaiter.domain.store.RegistrationReapplyRequest;
import jakarta.validation.Valid;

public interface RegistrationApplier {

  Registration apply(Account account, @Valid RegistrationApplyRequest applyRequest);

  Registration reapply(
      Long registrationId,
      Long accountId,
      @Valid RegistrationApplyRequest applyRequest
  );

  Registration reapply(
      Long registrationId,
      Long accountId,
      @Valid RegistrationReapplyRequest reapplyRequest
  );

}
