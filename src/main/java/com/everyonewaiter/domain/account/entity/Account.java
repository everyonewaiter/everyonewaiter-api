package com.everyonewaiter.domain.account.entity;

import com.everyonewaiter.domain.account.event.AccountCreateEvent;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "account")
@Entity
@Getter
@ToString(exclude = "password", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AggregateRoot<Account> {

  public enum State {INACTIVE, ACTIVE, DELETE}

  public enum Permission {USER, OWNER, ADMIN}

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.INACTIVE;

  @Enumerated(EnumType.STRING)
  @Column(name = "permission", nullable = false)
  private Permission permission = Permission.USER;

  @Column(name = "last_sign_in", nullable = false)
  private Instant lastSingIn = Instant.ofEpochMilli(0);

  public static Account create(String email, String password, String phoneNumber) {
    Account account = new Account();
    account.email = email;
    account.password = password;
    account.phoneNumber = phoneNumber;
    account.registerEvent(new AccountCreateEvent(email));
    account.registerEvent(new AuthMailSendEvent(email));
    return account;
  }

  public boolean isInactive() {
    return state == State.INACTIVE;
  }

}
