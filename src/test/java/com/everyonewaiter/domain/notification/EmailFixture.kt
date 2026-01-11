package com.everyonewaiter.domain.notification

import com.everyonewaiter.domain.ADMIN_EMAIL
import com.everyonewaiter.domain.shared.Email

fun createEmailTemplateReader(): EmailTemplateReader {
  return EmailTemplateReader { templateName, variables ->
    val contents = variables.entries.map { it.toString() }.toList()
    "${templateName}: ${contents.joinToString(separator = ",")}"
  }
}

fun createTemplateEmail(
  template: EmailTemplate = EmailTemplate.EMAIL_AUTHENTICATION,
  email: String = ADMIN_EMAIL,
  subject: String = "제목",
  variables: Map<String, Any> = mutableMapOf("authenticationUrl" to "https://auth.everyonewaiter.com")
): TemplateEmail {
  val templateEmail = TemplateEmail(template, Email(email), subject)
  variables.forEach { (key, value) -> templateEmail.addTemplateVariable(key, value) }
  return templateEmail
}
