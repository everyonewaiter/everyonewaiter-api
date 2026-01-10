package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.ADMIN_PHONE_NUMBER
import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthSuccess(
  purpose: AuthPurpose = AuthPurpose.SIGN_UP,
  phoneNumber: String = ADMIN_PHONE_NUMBER
): AuthSuccess {
  return AuthSuccess(purpose, PhoneNumber(phoneNumber))
}
