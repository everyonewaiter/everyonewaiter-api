package com.everyonewaiter.common.notification.mail

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.context.annotation.Import

@ImportAutoConfiguration(ThymeleafAutoConfiguration::class)
@Import(MailTemplateReader::class)
class MailTemplateReaderTest(
    private val templateReader: MailTemplateReader,
) : FunSpec({
        context("read") {
            test("타임리프 메일 템플릿을 읽어온다.") {
                val variables = mapOf("variable1" to "Hello", "variable2" to "World")
                templateReader.read("mail/template", variables) shouldBe "<p>Hello World</p>\n"
            }
        }
    })
