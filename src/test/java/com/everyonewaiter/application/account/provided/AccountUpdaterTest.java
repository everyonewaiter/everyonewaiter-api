package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountAdminUpdateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static com.everyonewaiter.domain.account.AccountPermission.ADMIN;
import static com.everyonewaiter.domain.account.AccountPermission.OWNER;
import static com.everyonewaiter.domain.account.AccountState.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
class AccountUpdaterTest extends IntegrationTest {

  private final AccountUpdater accountUpdater;
  private final AccountRepository accountRepository;

  @Test
  void authorize() {
    Account account = createAccount();

    account = accountUpdater.authorize(account.getId(), OWNER);

    assertThat(account.getPermission()).isEqualTo(OWNER);
  }

  @Test
  void authorizeFail() {
    assertThatThrownBy(() -> accountUpdater.authorize(999L, OWNER))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void updateByAdmin() {
    Account adminAccount = createAccount();

    ReflectionTestUtils.setField(adminAccount, "permission", ADMIN);

    Account userAccount = createAccount("user@gmail.com", "01087654321");

    userAccount = accountUpdater.updateByAdmin(
        adminAccount,
        userAccount.getId(),
        createAccountAdminUpdateRequest()
    );

    assertThat(userAccount.getState()).isEqualTo(ACTIVE);
    assertThat(userAccount.getPermission()).isEqualTo(OWNER);
  }

  private Account createAccount() {
    return createAccount("admin@everyonewaiter.com", "01012345678");
  }

  private Account createAccount(String email, String phoneNumber) {
    Account account = Account.create(
        createAccountCreateRequest(email, phoneNumber),
        createPasswordEncoder()
    );

    ReflectionTestUtils.setField(account, "state", ACTIVE);

    return accountRepository.save(account);
  }

  @Test
  void accountAdminUpdateRequestFail() {
    checkValidation(new AccountAdminUpdateRequest(null, null));
    checkValidation(new AccountAdminUpdateRequest(ACTIVE, null));
    checkValidation(new AccountAdminUpdateRequest(null, OWNER));
  }

  private void checkValidation(AccountAdminUpdateRequest updateRequest) {
    assertThatThrownBy(() -> accountUpdater.updateByAdmin(null, null, updateRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
