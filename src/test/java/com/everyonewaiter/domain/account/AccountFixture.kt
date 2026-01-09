package com.everyonewaiter.domain.account

import org.springframework.test.util.ReflectionTestUtils

fun createPasswordEncoder(): PasswordEncoder {
  return object : PasswordEncoder {
    override fun encode(rawPassword: String): String {
      return "(encoded) $rawPassword"
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
      return encode(rawPassword) == encodedPassword
    }
  }
}

fun createAccount(
  createRequest: AccountCreateRequest = createAccountCreateRequest(),
  passwordEncoder: PasswordEncoder = createPasswordEncoder(),
): Account {
  return Account.create(createRequest, passwordEncoder)
}

fun createActiveAccount(permission: AccountPermission = AccountPermission.USER): Account {
  val account = createAccount()
  account.activate()
  ReflectionTestUtils.setField(account, "permission", permission)
  return account
}

fun createAccountCreateRequest(
  email: String = "admin@everyonewaiter.com",
  password: String = "@password1",
  phoneNumber: String = "01012345678",
): AccountCreateRequest {
  return AccountCreateRequest(email, password, phoneNumber)
}

fun createAccountSignInRequest(
  email: String = "admin@everyonewaiter.com",
  password: String = "@password1",
): AccountSignInRequest {
  return AccountSignInRequest(email, password)
}

fun createAccountPasswordChangeRequest(
  currentPassword: String = "@password1",
  newPassword: String = "@password2",
): AccountPasswordChangeRequest {
  return AccountPasswordChangeRequest(currentPassword, newPassword)
}

fun createAccountAdminUpdateRequest(
  state: AccountState = AccountState.ACTIVE,
  permission: AccountPermission = AccountPermission.OWNER,
): AccountAdminUpdateRequest {
  return AccountAdminUpdateRequest(state, permission)
}
