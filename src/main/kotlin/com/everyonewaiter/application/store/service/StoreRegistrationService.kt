package com.everyonewaiter.application.store.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.application.image.service.ImageService
import com.everyonewaiter.application.store.dto.Apply
import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.repository.StoreRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreRegistrationService(
    private val imageService: ImageService,
    private val registrationRepository: StoreRegistrationRepository,
) {
    @Transactional
    fun apply(
        accountId: Long,
        request: Apply.Request,
    ): Long {
        val image = imageService.upload(ImageUpload.Request(request.file, "license"))
        val registration = StoreRegistration.create(
            accountId = accountId,
            name = request.name,
            ceoName = request.ceoName,
            address = request.address,
            landline = request.landline,
            license = request.license,
            image = image,
        )
        return registrationRepository.save(registration).id
    }
}
