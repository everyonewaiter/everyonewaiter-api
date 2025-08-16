package com.everyonewaiter.application.account.required;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static com.everyonewaiter.domain.account.AccountPermission.OWNER;
import static com.everyonewaiter.domain.account.AccountState.ACTIVE;
import static com.everyonewaiter.domain.account.AccountState.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminPageView;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class AccountRepositoryTest extends IntegrationTest {

  private final AccountRepository accountRepository;

  @Test
  void existsByEmail() {
    Email email = new Email("admin@everyonewaiter.com");

    assertThat(accountRepository.exists(email)).isFalse();

    createAccount();

    assertThat(accountRepository.exists(email)).isTrue();
  }

  @Test
  void existsStateByEmail() {
    Account account = createAccount();

    assertThat(accountRepository.existsState(account.getEmail(), INACTIVE)).isTrue();
    assertThat(accountRepository.existsState(account.getEmail(), ACTIVE)).isFalse();
  }

  @Test
  void existsByPhone() {
    PhoneNumber phoneNumber = new PhoneNumber("01012345678");

    assertThat(accountRepository.exists(phoneNumber)).isFalse();

    createAccount();

    assertThat(accountRepository.exists(phoneNumber)).isTrue();
  }

  @Test
  void existsStateByPhone() {
    Account account = createAccount();

    assertThat(accountRepository.existsState(account.getPhoneNumber(), INACTIVE)).isTrue();
    assertThat(accountRepository.existsState(account.getPhoneNumber(), ACTIVE)).isFalse();
  }

  @Test
  void findAllByAdmin() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAllByAdmin(new AccountAdminPageRequest());

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());
  }

  @Test
  void findAllByAdminWhereEmail() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAllByAdmin(
        new AccountAdminPageRequest(account.getEmail().address(), null, null, null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAllByAdmin(
        new AccountAdminPageRequest("user@everyonewaiter.com", null, null, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWhereState() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAllByAdmin(
        new AccountAdminPageRequest(null, account.getState(), null, null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAllByAdmin(
        new AccountAdminPageRequest(null, ACTIVE, null, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWherePermission() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAllByAdmin(
        new AccountAdminPageRequest(null, null, account.getPermission(), null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAllByAdmin(
        new AccountAdminPageRequest(null, null, OWNER, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWhereHasStore() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAllByAdmin(
        new AccountAdminPageRequest(null, null, null, false, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAllByAdmin(
        new AccountAdminPageRequest(null, null, null, true, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findById() {
    Account account = createAccount();

    assertThat(accountRepository.findById(account.getId())).isPresent();
    assertThat(accountRepository.findById(999L)).isEmpty();
  }

  @Test
  void findByIdOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findByIdOrThrow(account.getId()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findByIdOrThrow(999L))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findByEmail() {
    Account account = createAccount();

    assertThat(accountRepository.findByEmail(account.getEmail())).isPresent();
    assertThat(accountRepository.findByEmail(new Email("user@everyonewaiter.com"))).isEmpty();
  }

  @Test
  void findByEmailOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findByEmailOrThrow(account.getEmail()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findByEmailOrThrow(new Email("user@gmail.com")))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findByPhoneOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findByPhoneOrThrow(account.getPhoneNumber()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findByPhoneOrThrow(new PhoneNumber("01087654321")))
        .isInstanceOf(AccountNotFoundException.class);
  }

  private Account createAccount() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    return accountRepository.save(account);
  }

  private Paging<AccountAdminPageView> findAllByAdmin(AccountAdminPageRequest readRequest) {
    return accountRepository.findAllByAdmin(readRequest);
  }

}
