package com.everyonewaiter.application.image.dto

import org.springframework.web.multipart.MultipartFile

class ImageUpload {
    data class Request(
        val file: MultipartFile,
        val prefix: String?,
    )
}
