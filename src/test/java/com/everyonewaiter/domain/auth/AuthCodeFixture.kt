package com.everyonewaiter.domain.auth

import com.everyonewaiter.domain.shared.PhoneNumber

fun createAuthCode(phoneNumber: String = "01012345678", code: Int = 123456): AuthCode {
  return AuthCode(PhoneNumber(phoneNumber), code)
}
