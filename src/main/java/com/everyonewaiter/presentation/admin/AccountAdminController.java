package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.account.AccountService;
import com.everyonewaiter.application.account.response.AccountAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.admin.request.AccountAdminRead;
import com.everyonewaiter.presentation.admin.request.AccountAdminWrite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/accounts")
class AccountAdminController implements AccountAdminControllerSpecification {

  private final AccountService accountService;

  @Override
  @GetMapping
  public ResponseEntity<Paging<AccountAdmin.PageViewResponse>> getAccounts(
      @ModelAttribute @Valid AccountAdminRead.PageRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(accountService.readAllByAdmin(request.toAccountAdminPage()));
  }

  @Override
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountAdmin.ReadResponse> getAccount(
      @PathVariable Long accountId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    AccountAdmin.ReadResponse response = accountService.readByAdmin(accountId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PutMapping("/{accountId}")
  public ResponseEntity<Void> update(
      @PathVariable Long accountId,
      @RequestBody @Valid AccountAdminWrite.UpdateRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    accountService.updateByAdmin(accountId, request.toAccountAdminUpdate());
    return ResponseEntity.noContent().build();
  }

}
