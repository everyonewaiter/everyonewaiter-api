package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import jakarta.validation.Valid;

public interface AccountRegister {

  Account create(@Valid AccountCreateRequest createRequest);

  Account activate(String authMailToken);

}
