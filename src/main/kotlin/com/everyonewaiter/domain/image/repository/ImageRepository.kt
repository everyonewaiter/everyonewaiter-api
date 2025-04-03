package com.everyonewaiter.domain.image.repository

import com.everyonewaiter.domain.image.entity.Image

fun interface ImageRepository {
    fun save(image: Image): Image
}
