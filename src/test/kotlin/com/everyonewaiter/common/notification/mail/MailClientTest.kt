package com.everyonewaiter.common.notification.mail

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailClientTest(
    private val mailClient: MailClient,
) : FunSpec({
        xcontext("sendTo") {
            val receiver = "<TO>"
            val subject = "제목1"
            val content = "내용1"

            test("메일을 발송한다.") {
                mailClient.sendTo(receiver, subject, content)
            }
        }

        xcontext("sendBcc") {
            val receivers = listOf("<TO>", "<TO>")
            val subject = "제목2"
            val content = "내용2"

            test("메일을 발송한다.") {
                mailClient.sendTo(receivers, subject, content)
            }
        }
    })
