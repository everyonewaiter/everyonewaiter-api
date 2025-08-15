package com.everyonewaiter.domain.account;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.persistence.Entity;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "password", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Account extends AggregateRootEntity<Account> {

  private Email email;

  private String password;

  private PhoneNumber phoneNumber;

  private AccountState state;

  private AccountPermission permission;

  private Instant lastSignIn;

  public static Account create(
      AccountCreateRequest createRequest,
      PasswordEncoder passwordEncoder
  ) {
    Account account = new Account();

    account.email = new Email(createRequest.email());
    account.password = requireNonNull(passwordEncoder.encode(createRequest.password()));
    account.phoneNumber = new PhoneNumber(createRequest.phoneNumber());
    account.state = AccountState.INACTIVE;
    account.permission = AccountPermission.USER;
    account.lastSignIn = Instant.ofEpochMilli(0);

    account.registerEvent(new AccountCreateEvent(account.email));

    return account;
  }

  public boolean isInactive() {
    return state == AccountState.INACTIVE;
  }

  public boolean isActive() {
    return state == AccountState.ACTIVE;
  }

  public boolean hasPermission(AccountPermission permission) {
    return switch (permission) {
      case OWNER -> this.permission != AccountPermission.USER;
      case ADMIN -> this.permission == AccountPermission.ADMIN;
      default -> true;
    };
  }

  public void activate() {
    if (isInactive()) {
      this.state = AccountState.ACTIVE;
    } else {
      throw new AlreadyVerifiedEmailException();
    }
  }

  public void authorize(AccountPermission permission) {
    state(permission != AccountPermission.ADMIN, "관리자 권한은 부여할 수 없습니다.");

    if (!isActive()) {
      throw new DisabledAccountException();
    }

    if (!hasPermission(permission)) {
      this.permission = permission;
    }
  }

  public void signIn(AccountSignInRequest signInRequest, PasswordEncoder passwordEncoder) {
    if (isActive() && passwordEncoder.matches(signInRequest.password(), password)) {
      this.lastSignIn = Instant.now();
    } else {
      throw new FailedSignInException();
    }
  }

  public void update(Account userAccount, AccountAdminUpdateRequest updateRequest) {
    state(this.isActive() && this.hasPermission(AccountPermission.ADMIN), "관리자 권한이 없습니다.");

    userAccount.state = requireNonNull(updateRequest.state());
    userAccount.permission = requireNonNull(updateRequest.permission());
  }

}
