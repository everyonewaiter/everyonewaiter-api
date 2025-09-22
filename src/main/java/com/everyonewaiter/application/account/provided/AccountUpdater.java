package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import jakarta.validation.Valid;

public interface AccountUpdater {

  Account authorize(Long accountId, AccountPermission permission);

  Account updateByAdmin(
      Account adminAccount,
      Long userAccountId,
      @Valid AccountAdminUpdateRequest updateRequest
  );

}
