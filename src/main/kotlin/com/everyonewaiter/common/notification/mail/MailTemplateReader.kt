package com.everyonewaiter.common.notification.mail

import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.ISpringTemplateEngine

@Component
class MailTemplateReader(
    private val templateEngine: ISpringTemplateEngine,
) {
    fun read(
        template: String,
        variables: Map<String, String>,
    ): String {
        val context = Context().apply {
            variables.entries.forEach { (key, value) -> setVariable(key, value) }
        }
        return templateEngine.process(template, context)
    }
}
