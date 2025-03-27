package com.everyonewaiter.common.notification.message

data class NaverSensHeaderPayload(
    val method: String,
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val timestamp: String,
) {
    val plainSignatureText
        get() =
            """
            $method $url
            $timestamp
            $accessKey
            """.trimIndent()
}

data class AlimTalkSendPayload(
    val templateCode: String,
    val plusFriendId: String,
    val messages: List<AlimTalkMessage>,
)

data class AlimTalkMessage(
    val content: String,
    val to: String,
    val useSmsFailover: Boolean = true,
    val buttons: List<AlimTalkButton> = mutableListOf(),
)

@Suppress("UNUSED")
abstract class AlimTalkButton(
    val type: String,
    val name: String,
)

@Suppress("UNUSED")
class AlimTalkWebLinkButton(
    name: String,
    val linkMobile: String,
    val linkPc: String,
) : AlimTalkButton("WL", name)
