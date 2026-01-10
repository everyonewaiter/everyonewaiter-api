package com.everyonewaiter.domain.notification

import com.everyonewaiter.domain.ADMIN_PHONE_NUMBER
import com.everyonewaiter.domain.shared.PhoneNumber

fun createAlimTalkMessage(
  template: AlimTalkTemplate = AlimTalkTemplate.AUTHENTICATION_CODE,
  phoneNumber: String = ADMIN_PHONE_NUMBER,
  vararg variables: Any = Array(size = 1) { 123456 }
): AlimTalkMessage {
  return AlimTalkMessage(template, PhoneNumber(phoneNumber), *variables)
}
