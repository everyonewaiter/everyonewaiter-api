package com.everyonewaiter.domain.account;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "account",
    indexes = {
        @Index(name = "idx_account_phone_number", columnList = "phone_number"),
        @Index(name = "idx_account_state", columnList = "state"),
        @Index(name = "idx_account_permission_state", columnList = "permission, state"),
        @Index(name = "idx_account_email_state", columnList = "email, state"),
        @Index(name = "idx_account_email_permission_state", columnList = "email, permission, state"),
    }
)
@Getter
@ToString(exclude = "password", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Account extends AggregateRootEntity<Account> {

  @Embedded
  private Email email;

  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Embedded
  private PhoneNumber phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private AccountState state;

  @Enumerated(EnumType.STRING)
  @Column(name = "permission", nullable = false)
  private AccountPermission permission;

  @Column(name = "last_sign_in", nullable = false)
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

    account.registerEvent(new AccountCreateEvent(account));

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
    boolean isMatched = passwordEncoder.matches(signInRequest.password(), password);
    if (isActive() && isMatched) {
      this.lastSignIn = Instant.now();
      return;
    }

    if (isInactive() && isMatched) {
      throw new NotCompleteEmailVerificationException();
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
