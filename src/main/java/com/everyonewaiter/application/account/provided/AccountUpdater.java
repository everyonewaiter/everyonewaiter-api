package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import jakarta.validation.Valid;

public interface AccountUpdater {

  Account updateByAdmin(
      Account adminAccount,
      Long userAccountId,
      @Valid AccountAdminUpdateRequest updateRequest
  );

}
