package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.application.account.dto.AccountAdminReadRequest;
import com.everyonewaiter.application.account.dto.AccountAdminReadResponse;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;

public interface AccountFinder {

  Account find(Long accountId);

  Account find(PhoneNumber phoneNumber);

  Paging<AccountAdminReadResponse> findAllByAdmin(@Valid AccountAdminReadRequest readRequest);

}
