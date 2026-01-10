package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.ADMIN_PHONE_NUMBER
import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthCode(phoneNumber: String = ADMIN_PHONE_NUMBER, code: Int = 123456): AuthCode {
  return AuthCode(PhoneNumber(phoneNumber), code)
}
