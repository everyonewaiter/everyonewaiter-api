package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.account.service.AccountService;
import com.everyonewaiter.application.account.service.response.AccountAdmin;
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
  public ResponseEntity<AccountAdmin.ReadResponse> getAccount(
      @PathVariable Long accountId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    AccountAdmin.ReadResponse response = accountService.readByAdmin(accountId);
    return ResponseEntity.ok(response);
  }

}
