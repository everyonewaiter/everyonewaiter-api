package com.everyonewaiter.infrastructure.image

import org.springframework.web.multipart.MultipartFile
import java.util.Date
import java.util.concurrent.CompletableFuture

interface ImageClient {
    fun read(
        name: String,
        accessUrlExpiration: Date,
    ): CompletableFuture<String>

    fun upload(file: MultipartFile)
}
