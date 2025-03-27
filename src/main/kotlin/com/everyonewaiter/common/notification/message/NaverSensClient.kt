package com.everyonewaiter.common.notification.message

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "message-client",
    url = "https://sens.apigw.ntruss.com",
    configuration = [NaverSensProperties::class],
)
fun interface NaverSensClient {
    @PostMapping(value = ["/alimtalk/v2/services/{serviceId}/messages"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun sendAlimTalk(
        @PathVariable("serviceId") serviceId: String,
        @RequestBody payload: AlimTalkSendPayload,
    )
}
