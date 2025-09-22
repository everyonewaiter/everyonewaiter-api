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
  void existsByEmailAndState() {
    Account account = createAccount();

    assertThat(accountRepository.exists(account.getEmail(), INACTIVE)).isTrue();
    assertThat(accountRepository.exists(account.getEmail(), ACTIVE)).isFalse();
  }

  @Test
  void existsByPhone() {
    PhoneNumber phoneNumber = new PhoneNumber("01012345678");

    assertThat(accountRepository.exists(phoneNumber)).isFalse();

    createAccount();

    assertThat(accountRepository.exists(phoneNumber)).isTrue();
  }

  @Test
  void existsByPhoneAndState() {
    Account account = createAccount();

    assertThat(accountRepository.exists(account.getPhoneNumber(), INACTIVE)).isTrue();
    assertThat(accountRepository.exists(account.getPhoneNumber(), ACTIVE)).isFalse();
  }

  @Test
  void findAll() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAll(new AccountAdminPageRequest());

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());
  }

  @Test
  void findAllWhereEmail() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAll(
        new AccountAdminPageRequest(account.getEmail().address(), null, null, null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAll(
        new AccountAdminPageRequest("user@everyonewaiter.com", null, null, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllWhereState() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAll(
        new AccountAdminPageRequest(null, account.getState(), null, null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAll(
        new AccountAdminPageRequest(null, ACTIVE, null, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllWherePermission() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAll(
        new AccountAdminPageRequest(null, null, account.getPermission(), null, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAll(
        new AccountAdminPageRequest(null, null, OWNER, null, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findAllWhereHasStore() {
    Account account = createAccount();

    Paging<AccountAdminPageView> pagedViews = findAll(
        new AccountAdminPageRequest(null, null, null, false, 1, 20)
    );

    assertThat(pagedViews.getContent()).hasSize(1);
    assertThat(pagedViews.getContent().getFirst().id()).isEqualTo(account.getId());

    Paging<AccountAdminPageView> emptyViews = findAll(
        new AccountAdminPageRequest(null, null, null, true, 1, 20)
    );

    assertThat(emptyViews.getContent()).isEmpty();
  }

  @Test
  void findById() {
    Account account = createAccount();

    assertThat(accountRepository.find(account.getId())).isPresent();
    assertThat(accountRepository.find(999L)).isEmpty();
  }

  @Test
  void findByIdOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findOrThrow(account.getId()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findOrThrow(999L))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findByEmail() {
    Account account = createAccount();

    assertThat(accountRepository.find(account.getEmail())).isPresent();
    assertThat(accountRepository.find(new Email("user@everyonewaiter.com"))).isEmpty();
  }

  @Test
  void findByEmailOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findOrThrow(account.getEmail()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findOrThrow(new Email("user@gmail.com")))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void findByPhoneOrThrow() {
    Account account = createAccount();

    assertThatCode(() -> accountRepository.findOrThrow(account.getPhoneNumber()))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountRepository.findOrThrow(new PhoneNumber("01087654321")))
        .isInstanceOf(AccountNotFoundException.class);
  }

  private Account createAccount() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    return accountRepository.save(account);
  }

  private Paging<AccountAdminPageView> findAll(AccountAdminPageRequest readRequest) {
    return accountRepository.findAll(readRequest);
  }

}
