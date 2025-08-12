package com.everyonewaiter.domain.account.entity;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.account.event.AccountCreateEvent;
import com.everyonewaiter.domain.auth.AuthMailSendEvent;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.persistence.Entity;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@ToString(exclude = "password", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Account extends AggregateRootEntity<Account> {

  public enum State {INACTIVE, ACTIVE, DELETE}

  public enum Permission {USER, OWNER, ADMIN}

  private Email email;

  private String password;

  private PhoneNumber phoneNumber;

  private State state = State.INACTIVE;

  private Permission permission = Permission.USER;

  private Instant lastSignIn = Instant.ofEpochMilli(0);

  public static Account create(String email, String password, String phoneNumber) {
    Account account = new Account();
    account.email = new Email(email);
    account.password = password;
    account.phoneNumber = new PhoneNumber(phoneNumber);
    account.registerEvent(new AccountCreateEvent(email));
    account.registerEvent(new AuthMailSendEvent(new Email(email)));
    return account;
  }

  public boolean isInactive() {
    return state == State.INACTIVE;
  }

  public boolean isActive() {
    return state == State.ACTIVE;
  }

  public boolean hasPermission(Permission permission) {
    return switch (permission) {
      case OWNER -> this.permission != Permission.USER;
      case ADMIN -> this.permission == Permission.ADMIN;
      default -> true;
    };
  }

  public void activate() {
    if (isInactive()) {
      this.state = State.ACTIVE;
    } else {
      throw new BusinessException(ErrorCode.ALREADY_VERIFIED_EMAIL);
    }
  }

  public void authorize(Permission permission) {
    if (!isActive()) {
      throw new BusinessException(ErrorCode.DISABLED_ACCOUNT);
    }
    if (!hasPermission(permission)) {
      this.permission = permission;
    }
  }

  public void signIn(PasswordEncoder passwordEncoder, String rawPassword) {
    if (isActive() && passwordEncoder.matches(rawPassword, password)) {
      this.lastSignIn = Instant.now();
    } else {
      throw new BusinessException(ErrorCode.FAILED_SIGN_IN);
    }
  }

  public void update(State state, Permission permission) {
    this.state = state;
    this.permission = permission;
  }

}
