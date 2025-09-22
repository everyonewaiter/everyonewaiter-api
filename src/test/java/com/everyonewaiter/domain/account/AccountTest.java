package com.everyonewaiter.domain.account;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountAdminUpdateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createAccountSignInRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static com.everyonewaiter.domain.account.AccountPermission.ADMIN;
import static com.everyonewaiter.domain.account.AccountPermission.OWNER;
import static com.everyonewaiter.domain.account.AccountPermission.USER;
import static com.everyonewaiter.domain.account.AccountState.ACTIVE;
import static com.everyonewaiter.domain.account.AccountState.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AccountTest {

  @Test
  void create() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThat(account.getState()).isEqualTo(INACTIVE);
    assertThat(account.getPermission()).isEqualTo(USER);
    assertThat(account.getLastSignIn()).isEqualTo(Instant.ofEpochMilli(0));

    Object domainEvents = ReflectionTestUtils.invokeGetterMethod(account, "domainEvents");
    assertThat(domainEvents).isInstanceOf(List.class);
    assertThat((List<?>) domainEvents).hasSize(1);
  }

  @Test
  void isInactive() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThat(account.isInactive()).isTrue();

    ReflectionTestUtils.setField(account, "state", ACTIVE);

    assertThat(account.isInactive()).isFalse();
  }

  @Test
  void isActive() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThat(account.isActive()).isFalse();

    ReflectionTestUtils.setField(account, "state", ACTIVE);

    assertThat(account.isActive()).isTrue();
  }

  @Test
  void hasPermission() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThat(account.hasPermission(USER)).isTrue();
    assertThat(account.hasPermission(OWNER)).isFalse();
    assertThat(account.hasPermission(ADMIN)).isFalse();

    ReflectionTestUtils.setField(account, "permission", OWNER);

    assertThat(account.hasPermission(USER)).isTrue();
    assertThat(account.hasPermission(OWNER)).isTrue();
    assertThat(account.hasPermission(ADMIN)).isFalse();

    ReflectionTestUtils.setField(account, "permission", ADMIN);

    assertThat(account.hasPermission(USER)).isTrue();
    assertThat(account.hasPermission(OWNER)).isTrue();
    assertThat(account.hasPermission(ADMIN)).isTrue();
  }

  @Test
  void activate() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThat(account.getState()).isEqualTo(INACTIVE);

    account.activate();

    assertThat(account.getState()).isEqualTo(ACTIVE);

    assertThatThrownBy(account::activate).isInstanceOf(AlreadyVerifiedEmailException.class);
  }

  @Test
  void authorize() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    account.activate();
    account.authorize(OWNER);

    assertThat(account.getPermission()).isEqualTo(OWNER);
  }

  @Test
  void authorizeFail() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    assertThatThrownBy(() -> account.authorize(OWNER)).isInstanceOf(DisabledAccountException.class);

    account.activate();

    assertThatThrownBy(() -> account.authorize(ADMIN)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void signIn() {
    PasswordEncoder passwordEncoder = createPasswordEncoder();

    Account account = Account.create(createAccountCreateRequest(), passwordEncoder);

    account.activate();
    account.signIn(createAccountSignInRequest(), passwordEncoder);

    assertThat(account.getLastSignIn()).isNotEqualTo(Instant.ofEpochMilli(0));
  }

  @Test
  void signInFail() {
    PasswordEncoder passwordEncoder = createPasswordEncoder();

    Account account = Account.create(createAccountCreateRequest(), passwordEncoder);

    assertThatThrownBy(() -> account.signIn(createAccountSignInRequest(), passwordEncoder))
        .isInstanceOf(FailedSignInException.class);

    account.activate();

    assertThatThrownBy(() -> account.signIn(createAccountSignInRequest("invalid"), passwordEncoder))
        .isInstanceOf(FailedSignInException.class);
  }

  @Test
  void update() {
    Account adminAccount = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    ReflectionTestUtils.setField(adminAccount, "state", ACTIVE);
    ReflectionTestUtils.setField(adminAccount, "permission", ADMIN);

    Account userAccount = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    adminAccount.update(userAccount, createAccountAdminUpdateRequest());

    assertThat(userAccount.getState()).isEqualTo(ACTIVE);
    assertThat(userAccount.getPermission()).isEqualTo(OWNER);
  }

  @Test
  void updateFail() {
    Account adminAccount = Account.create(createAccountCreateRequest(), createPasswordEncoder());
    Account userAccount = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    ReflectionTestUtils.setField(adminAccount, "state", INACTIVE);
    ReflectionTestUtils.setField(adminAccount, "permission", ADMIN);

    assertThatThrownBy(() -> adminAccount.update(userAccount, createAccountAdminUpdateRequest()))
        .isInstanceOf(IllegalStateException.class);

    ReflectionTestUtils.setField(adminAccount, "state", ACTIVE);
    ReflectionTestUtils.setField(adminAccount, "permission", OWNER);

    assertThatThrownBy(() -> adminAccount.update(userAccount, createAccountAdminUpdateRequest()))
        .isInstanceOf(IllegalStateException.class);
  }

}
