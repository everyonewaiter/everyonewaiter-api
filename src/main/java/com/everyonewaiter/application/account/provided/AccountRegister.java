package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import com.everyonewaiter.domain.shared.Email;
import jakarta.validation.Valid;

public interface AccountRegister {

  Account register(@Valid AccountCreateRequest createRequest);

  Account activate(Email email);

}
