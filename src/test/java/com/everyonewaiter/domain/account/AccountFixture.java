package com.everyonewaiter.domain.account;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AccountFixture {

  public static PasswordEncoder createPasswordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(String rawPassword) {
        return "(encoded) " + rawPassword;
      }

      @Override
      public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
      }
    };
  }

  public static AccountCreateRequest createAccountCreateRequest() {
    return createAccountCreateRequest("admin@everyonewaiter.com");
  }

  public static AccountCreateRequest createAccountCreateRequest(String email) {
    return createAccountCreateRequest(email, "01012345678");
  }

  public static AccountCreateRequest createAccountCreateRequest(String email, String phoneNumber) {
    return new AccountCreateRequest(email, "@password1", phoneNumber);
  }

  public static AccountSignInRequest createAccountSignInRequest() {
    return createAccountSignInRequest("@password1");
  }

  public static AccountSignInRequest createAccountSignInRequest(String password) {
    return new AccountSignInRequest("admin@everyonewaiter.com", password);
  }

  public static AccountAdminUpdateRequest createAccountAdminUpdateRequest() {
    return new AccountAdminUpdateRequest(AccountState.ACTIVE, AccountPermission.OWNER);
  }

}
