package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthSuccess(
  purpose: AuthPurpose = AuthPurpose.SIGN_UP,
  phoneNumber: String = "01012345678"
): AuthSuccess {
  return AuthSuccess(purpose, PhoneNumber(phoneNumber))
}
