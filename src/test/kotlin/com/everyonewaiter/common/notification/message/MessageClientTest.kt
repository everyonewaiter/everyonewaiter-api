package com.everyonewaiter.common.notification.message

import com.everyonewaiter.support.IntegrationTest
import io.kotest.core.spec.style.FunSpec

@IntegrationTest
class MessageClientTest(
    private val messageClient: MessageClient,
) : FunSpec({
        xcontext("sendAlimTalk") {
            val receiver = "<TO>"
            val templateCode = "<TEMPLATE_CODE>"
            val messages = AlimTalkMessage(
                content = "<CONTENT>",
                to = receiver,
                buttons = listOf(
                    AlimTalkWebLinkButton(
                        name = "<BUTTON_NAME>",
                        linkMobile = "<LINK_MOBILE>",
                        linkPc = "<LINK_PC>",
                    ),
                ),
            )

            test("알림톡을 전송한다.") {
                messageClient.sendAlimTalk(templateCode, messages)
            }
        }
    })
