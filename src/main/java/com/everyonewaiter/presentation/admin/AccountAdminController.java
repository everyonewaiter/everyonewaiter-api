package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.account.service.AccountService;
import com.everyonewaiter.application.account.service.response.AccountAdminRead;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/accounts")
class AccountAdminController implements AccountAdminControllerSpecification {

  private final AccountService accountService;

  @Override
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountAdminRead.Response> getAccount(
      @PathVariable Long accountId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    AccountAdminRead.Response response = accountService.readByAdmin(accountId);
    return ResponseEntity.ok(response);
  }

}
