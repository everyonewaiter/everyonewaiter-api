package com.everyonewaiter.common.notification.message

import com.everyonewaiter.common.signature.SignatureEncoder
import feign.RequestInterceptor
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val ACCESS_KEY_HEADER = "x-ncp-iam-access-key"
private const val SIGNATURE_HEADER = "x-ncp-apigw-signature-v2"
private const val TIMESTAMP_HEADER = "x-ncp-apigw-timestamp"

private val logger = KotlinLogging.logger {}

@Configuration
class NaverSensProperties(
    @Value("\${naver.sens.access-key}") val accessKey: String,
    @Value("\${naver.sens.secret-key}") val secretKey: String,
    @Value("\${naver.sens.service-id}") val serviceId: String,
    @Value("\${naver.sens.channel-id}") val channelId: String,
    private val signatureEncoder: SignatureEncoder,
) {
    @Bean
    fun requestInterceptor(): RequestInterceptor =
        RequestInterceptor { requestTemplate ->
            val payload = NaverSensHeaderPayload(
                method = requestTemplate.method(),
                url = requestTemplate.url(),
                accessKey = accessKey,
                secretKey = secretKey,
                timestamp = System.currentTimeMillis().toString(),
            )
            val signature = signatureEncoder
                .encode(payload.plainSignatureText, payload.secretKey)
                .onFailure { logger.warn { "알림톡 전송 요청 중 시그니처 생성에 실패하였습니다: ${it.message}" } }
                .getOrThrow()
            requestTemplate.header(ACCESS_KEY_HEADER, payload.accessKey)
            requestTemplate.header(SIGNATURE_HEADER, signature)
            requestTemplate.header(TIMESTAMP_HEADER, payload.timestamp)
        }
}
