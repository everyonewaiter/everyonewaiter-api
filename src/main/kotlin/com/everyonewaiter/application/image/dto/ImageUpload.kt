package com.everyonewaiter.application.image.dto

import org.springframework.web.multipart.MultipartFile

class ImageUpload {
    data class Request(
        val prefix: String,
        val accountId: Long,
        val file: MultipartFile,
    )
}
