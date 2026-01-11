package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.ADMIN_PHONE_NUMBER
import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthAttempt(
  purpose: AuthPurpose = AuthPurpose.SIGN_UP,
  phoneNumber: String = ADMIN_PHONE_NUMBER
): AuthAttempt {
  return AuthAttempt(purpose, PhoneNumber(phoneNumber))
}
