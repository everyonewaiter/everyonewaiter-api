package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.domain.image.entity.Image
import org.springframework.data.repository.CrudRepository

interface ImageJdbcRepository : CrudRepository<Image, Long>
