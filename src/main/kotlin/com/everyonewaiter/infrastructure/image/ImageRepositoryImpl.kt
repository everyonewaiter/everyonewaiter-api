package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.domain.image.entity.Image
import com.everyonewaiter.domain.image.repository.ImageRepository
import org.springframework.stereotype.Repository

@Repository
class ImageRepositoryImpl(
    private val imageJdbcRepository: ImageJdbcRepository,
) : ImageRepository {
    override fun save(image: Image): Image = imageJdbcRepository.save(image)
}
