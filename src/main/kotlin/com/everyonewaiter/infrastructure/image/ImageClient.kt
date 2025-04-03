package com.everyonewaiter.infrastructure.image

import java.io.File
import java.util.Date

interface ImageClient {
    fun read(
        name: String,
        accessUrlExpiration: Date,
    ): String

    fun upload(image: File)
}
