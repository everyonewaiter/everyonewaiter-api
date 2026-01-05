package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthAttempt(
  purpose: AuthPurpose = AuthPurpose.SIGN_UP,
  phoneNumber: String = "01012345678"
): AuthAttempt {
  return AuthAttempt(purpose, PhoneNumber(phoneNumber))
}
