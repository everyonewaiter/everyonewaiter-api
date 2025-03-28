package com.everyonewaiter.global.extension

import jakarta.servlet.http.HttpServletRequest

private const val NULL_CONTENT = "NULL"
private const val MASKED_CONTENT = "BLIND"

val HttpServletRequest.xRequestId: String
    get() = getHeader("x-request-id") ?: requestId

val HttpServletRequest.requestUri: String
    get() = if (queryString.isNullOrBlank()) {
        requestURI
    } else {
        "$requestURI?$queryString"
    }

val HttpServletRequest.parameters: String
    get() {
        val parameters = StringBuilder()
        parameterNames
            .asIterator()
            .forEachRemaining { paramName: String ->
                parameters.append("$paramName: ${getParameter(paramName)}\n")
            }
        return parameters.toString().ifBlank { NULL_CONTENT }
    }

val HttpServletRequest.headers: String
    get() {
        val headers = StringBuilder()
        headerNames
            .asIterator()
            .forEachRemaining { headerName: String ->
                val needMasking = headerName.equals("Authorization", ignoreCase = true)
                val content = if (needMasking) MASKED_CONTENT else getHeader(headerName)
                headers.append("$headerName: $content$\n")
            }
        return headers.toString().ifBlank { NULL_CONTENT }
    }
