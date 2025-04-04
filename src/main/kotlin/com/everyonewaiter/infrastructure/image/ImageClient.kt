package com.everyonewaiter.infrastructure.image

import org.springframework.web.multipart.MultipartFile

fun interface ImageClient {
    fun upload(file: MultipartFile): String
}
