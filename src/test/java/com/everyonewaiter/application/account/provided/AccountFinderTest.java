package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminReadRequest;
import com.everyonewaiter.domain.account.AccountAdminView;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class AccountFinderTest extends IntegrationTest {

  private final AccountFinder accountFinder;
  private final AccountRepository accountRepository;

  @Test
  void find() {
    Account account = createAccount();

    Optional<Account> found = accountFinder.find(account.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getId()).isEqualTo(account.getId());
  }

  @Test
  void findByIdOrThrow() {
    Account account = createAccount();

    Account found = accountFinder.findOrThrow(account.getId());

    assertThat(found.getId()).isEqualTo(account.getId());
  }

  @Test
  void findByIdOrThrowFail() {
    assertThatThrownBy(() -> accountFinder.findOrThrow(999L))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findByPhoneOrThrow() {
    Account account = createAccount();

    Account found = accountFinder.findOrThrow(account.getPhoneNumber());

    assertThat(found.getId()).isEqualTo(account.getId());
  }

  @Test
  void findByPhoneOrThrowFail() {
    assertThatThrownBy(() -> accountFinder.findOrThrow(new PhoneNumber("01087654321")))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findAllByAdmin() {
    Account account = createAccount();

    Paging<AccountAdminView> views = accountFinder.findAllByAdmin(new AccountAdminReadRequest());

    assertThat(views.getContent()).hasSize(1);
    assertThat(views.getContent().getFirst().id()).isEqualTo(account.getId());
  }

  private Account createAccount() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    return accountRepository.save(account);
  }

  @Test
  void accountAdminReadRequestFail() {
    checkValidation(new AccountAdminReadRequest(null, null, null, null, 0, 1));
    checkValidation(new AccountAdminReadRequest(null, null, null, null, 1, 0));
  }

  private void checkValidation(AccountAdminReadRequest readRequest) {
    assertThatThrownBy(() -> accountFinder.findAllByAdmin(readRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
