package com.everyonewaiter.global.extension

import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

private const val NULL_CONTENT = "NULL"
private const val MASKED_CONTENT = "BLIND"

val ContentCachingRequestWrapper.xRequestId: String
    get() = getHeader("x-request-id") ?: requestId

val ContentCachingRequestWrapper.requestUri: String
    get() = if (queryString.isNullOrBlank()) {
        requestURI
    } else {
        "$requestURI?$queryString"
    }

val ContentCachingRequestWrapper.parameters: String
    get() {
        val parameters = StringBuilder()
        parameterNames
            .asIterator()
            .forEachRemaining { paramName: String ->
                parameters.append("$paramName: ${getParameter(paramName)}\n")
            }
        return parameters.toString().ifBlank { NULL_CONTENT }
    }

val ContentCachingRequestWrapper.headers: String
    get() {
        val headers = StringBuilder()
        headerNames
            .asIterator()
            .forEachRemaining { headerName: String ->
                val needMasking = needMaskingContent(headerName, "Authorization")
                val content = if (needMasking) MASKED_CONTENT else getHeader(headerName)
                headers.append("$headerName: $content$\n")
            }
        return headers.toString().ifBlank { NULL_CONTENT }
    }

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
