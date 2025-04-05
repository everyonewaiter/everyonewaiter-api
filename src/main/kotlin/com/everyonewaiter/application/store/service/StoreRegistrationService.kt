package com.everyonewaiter.application.store.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.application.image.service.ImageService
import com.everyonewaiter.application.store.dto.Apply
import com.everyonewaiter.application.store.dto.Registration
import com.everyonewaiter.domain.store.entity.BusinessLicenseInformation
import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.repository.StoreRegistrationRepository
import com.everyonewaiter.global.extension.calculatePageLimit
import com.everyonewaiter.global.extension.calculatePageOffset
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
            licenseInformation = BusinessLicenseInformation(
                name = request.name,
                ceoName = request.ceoName,
                address = request.address,
                landline = request.landline,
                license = request.license,
                image = image,
            ),
        )
        return registrationRepository.save(registration).id
    }

    fun getRegistration(
        registrationId: Long,
        accountId: Long,
    ): Registration.Response {
        val registration = registrationRepository.findOneOrThrow(registrationId, accountId)
        return Registration.Response.from(registration)
    }

    fun getRegistrations(
        accountId: Long,
        request: Registration.PageRequest,
    ): Registration.PageResponse {
        val (page, size) = request
        val registrations = registrationRepository.findAll(accountId, size, calculatePageOffset(page, size))
        val registrationCount = registrationRepository.count(accountId, calculatePageLimit(page, size))
        return Registration.PageResponse.of(registrations, registrationCount)
    }
}
