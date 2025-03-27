package com.everyonewaiter.common.notification.mail

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailClientTest(
    private val mailClient: MailClient,
) : FunSpec({
        xcontext("메일을 발송한다.") {
            val receivers = listOf("<TO>", "<TO>")

            test("sendTo") {
                mailClient.sendTo(receivers[0], "제목1", "내용1")
            }

            test("sendBcc") {
                mailClient.sendTo(receivers, "제목2", "내용2")
            }
        }
    })
