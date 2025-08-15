package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountAdminUpdateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
class AccountUpdaterTest extends IntegrationTest {

  private final AccountUpdater accountUpdater;
  private final AccountRepository accountRepository;

  @Test
  void updateByAdmin() {
    Account adminAccount = createAccount();

    ReflectionTestUtils.setField(adminAccount, "state", AccountState.ACTIVE);
    ReflectionTestUtils.setField(adminAccount, "permission", AccountPermission.ADMIN);

    Account userAccount = createAccount("user@gmail.com", "01087654321");

    userAccount = accountUpdater.updateByAdmin(
        adminAccount,
        userAccount.getId(),
        createAccountAdminUpdateRequest()
    );

    assertThat(userAccount.getState()).isEqualTo(AccountState.ACTIVE);
    assertThat(userAccount.getPermission()).isEqualTo(AccountPermission.OWNER);
  }

  private Account createAccount() {
    return createAccount("admin@everyonewaiter.com", "01012345678");
  }

  private Account createAccount(String email, String phoneNumber) {
    Account account = Account.create(
        createAccountCreateRequest(email, phoneNumber),
        createPasswordEncoder()
    );

    return accountRepository.save(account);
  }

  @Test
  void accountAdminUpdateRequestFail() {
    checkValidation(new AccountAdminUpdateRequest(null, null));
    checkValidation(new AccountAdminUpdateRequest(AccountState.ACTIVE, null));
    checkValidation(new AccountAdminUpdateRequest(null, AccountPermission.OWNER));
  }

  private void checkValidation(AccountAdminUpdateRequest updateRequest) {
    assertThatThrownBy(() -> accountUpdater.updateByAdmin(null, null, updateRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
