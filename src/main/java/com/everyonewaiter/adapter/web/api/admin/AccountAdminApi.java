package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.api.dto.AccountAdminPageResponse;
import com.everyonewaiter.adapter.web.api.dto.AccountDetailResponse;
import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.provided.AccountUpdater;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.Paging;
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
@RequestMapping("/v1/admins/accounts")
class AccountAdminApi implements AccountAdminApiSpecification {

  private final AccountFinder accountFinder;
  private final AccountUpdater accountUpdater;

  @Override
  @GetMapping
  public ResponseEntity<Paging<AccountAdminPageResponse>> getAccounts(
      @ModelAttribute @Valid AccountAdminPageRequest pageRequest,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(
        accountFinder.findAllByAdmin(pageRequest).map(AccountAdminPageResponse::from)
    );
  }

  @Override
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountDetailResponse> getAccount(
      @PathVariable Long accountId,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    Account foundAccount = accountFinder.findOrThrow(accountId);

    return ResponseEntity.ok(AccountDetailResponse.from(foundAccount));
  }

  @Override
  @PutMapping("/{accountId}")
  public ResponseEntity<Void> update(
      @PathVariable Long accountId,
      @RequestBody @Valid AccountAdminUpdateRequest updateRequest,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    accountUpdater.updateByAdmin(account, accountId, updateRequest);

    return ResponseEntity.noContent().build();
  }

}
