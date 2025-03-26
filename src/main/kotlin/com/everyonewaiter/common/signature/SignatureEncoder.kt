package com.everyonewaiter.common.signature

import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val HMAC_SHA256 = "HmacSHA256"

/**
 * 시그니처를 생성하고 검증하는 클래스입니다.
 */
@Component
class SignatureEncoder {
    /**
     * 시크릿 키를 사용하여 주어진 문자열을 HMAC SHA256으로 서명(인코딩)합니다.
     *
     * @param [plainText] 인코딩할 문자열
     * @param [secretKey] 시크릿 키
     * @return 인코딩된 문자열
     */
    fun encode(
        plainText: String,
        secretKey: String,
    ): Result<String> =
        runCatching {
            val hmac = Mac.getInstance(HMAC_SHA256).apply {
                init(SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), HMAC_SHA256))
            }
            val rawHmac = hmac.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().encodeToString(rawHmac)
        }

    /**
     * 서명(인코딩)된 문자열이 주어진 문자열과 시크릿 키로 생성된 것인지 확인합니다.
     *
     * @param [encodedText] 인코딩된 문자열
     * @param [plainText] 확인할 문자열
     * @param [secretKey] 시크릿 키
     */
    fun matches(
        encodedText: String,
        plainText: String,
        secretKey: String,
    ) = encode(plainText, secretKey).getOrElse { return false } == encodedText
}
