package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.account.AlreadyUseEmailException;
import com.everyonewaiter.domain.account.AlreadyUsePhoneException;
import com.everyonewaiter.domain.account.AlreadyVerifiedEmailException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
class AccountValidatorTest extends IntegrationTest {

  private final AccountValidator accountValidator;
  private final AccountRepository accountRepository;

  @Test
  void checkDuplicateEmail() {
    Account account = createAccount();

    assertThatCode(() -> accountValidator.checkDuplicateEmail(new Email("user@gmail.com")))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountValidator.checkDuplicateEmail(account.getEmail()))
        .isInstanceOf(AlreadyUseEmailException.class);
  }

  @Test
  void checkDuplicatePhone() {
    Account account = createAccount();

    assertThatCode(() -> accountValidator.checkDuplicatePhone(new PhoneNumber("01087654321")))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> accountValidator.checkDuplicatePhone(account.getPhoneNumber()))
        .isInstanceOf(AlreadyUsePhoneException.class);
  }

  @Test
  void checkPossibleSendAuthMail() {
    Account account = createAccount();

    assertThatCode(() -> accountValidator.checkPossibleSendAuthMail(account.getEmail()))
        .doesNotThrowAnyException();
  }

  @Test
  void checkPossibleSendAuthMailFail() {
    Account account = createAccount(AccountState.ACTIVE, AccountPermission.USER);

    assertThatThrownBy(() -> accountValidator.checkPossibleSendAuthMail(account.getEmail()))
        .isInstanceOf(AlreadyVerifiedEmailException.class);
  }

  @Test
  void checkPossibleSendAuthCode() {
    Account account = createAccount(AccountState.ACTIVE, AccountPermission.OWNER);

    assertThatCode(() -> accountValidator.checkPossibleSendAuthCode(account.getPhoneNumber()))
        .doesNotThrowAnyException();
  }

  @Test
  void checkPossibleSendAuthCodeFail() {
    Account account = createAccount(AccountState.INACTIVE, AccountPermission.OWNER);

    assertThatThrownBy(() -> accountValidator.checkPossibleSendAuthCode(account.getPhoneNumber()))
        .isInstanceOf(AccountNotFoundException.class);
  }

  private Account createAccount() {
    return createAccount(AccountState.INACTIVE, AccountPermission.USER);
  }

  private Account createAccount(AccountState state, AccountPermission permission) {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    ReflectionTestUtils.setField(account, "state", state);
    ReflectionTestUtils.setField(account, "permission", permission);

    return accountRepository.save(account);
  }

}
