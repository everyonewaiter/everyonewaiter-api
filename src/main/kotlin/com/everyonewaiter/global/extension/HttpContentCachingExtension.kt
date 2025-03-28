package com.everyonewaiter.global.extension

import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

private const val NULL_CONTENT = "NULL"
private const val MASKED_CONTENT = "BLIND"

val ContentCachingRequestWrapper.body: String
    get() {
        val contentBytes = contentAsByteArray
        return if (contentBytes.isNotEmpty()) {
            val content = String(contentBytes, StandardCharsets.UTF_8)
            val needMasking = needMaskingContent(content, "password")
            if (needMasking) MASKED_CONTENT else content
        } else {
            NULL_CONTENT
        }
    }

val ContentCachingResponseWrapper.body: String
    get() {
        val contentBytes = contentAsByteArray
        copyBodyToResponse()
        return if (contentBytes.isNotEmpty()) {
            val content = String(contentBytes, StandardCharsets.UTF_8)
            val needMasking = needMaskingContent(content, "password", "token", "secretKey")
            if (needMasking) MASKED_CONTENT else content
        } else {
            NULL_CONTENT
        }
    }

private fun needMaskingContent(
    content: String,
    vararg keywords: String,
): Boolean = keywords.any { content.contains(it, ignoreCase = true) }
