package com.everyonewaiter.application.store.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.application.image.service.ImageService
import com.everyonewaiter.application.store.dto.Apply
import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.domain.store.entity.BusinessLicenseInformation
import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.repository.StoreRegistrationRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class StoreRegistrationServiceTest :
    FunSpec({
        val imageService = mockk<ImageService>()
        val registrationRepository = mockk<StoreRegistrationRepository>()
        val registrationService = StoreRegistrationService(
            imageService = imageService,
            registrationRepository = registrationRepository,
        )

        context("apply") {
            val accountId = 1L
            val request = Apply.Request(
                name = "홍길동식당",
                ceoName = "홍길동",
                address = "서울시 강남구",
                landline = "02-123-4567",
                license = "111-11-11111",
                file = SimpleMultipartFile("abc.jpg", ByteArray(0), "abc.jpg", "image/jpeg"),
            )
            val registration = StoreRegistration(
                id = Tsid.nextLong(),
                accountId = accountId,
                licenseInformation = BusinessLicenseInformation(
                    name = request.name,
                    ceoName = request.ceoName,
                    address = request.address,
                    landline = request.landline,
                    license = request.license,
                    image = "license/202501/abc.webp",
                ),
            )

            test("매장 등록을 신청한다.") {
                every {
                    imageService.upload(
                        ImageUpload.Request(
                            request.file,
                            "license",
                        ),
                    )
                } returns registration.licenseInformation.image
                every { registrationRepository.save(any()) } returns registration
                val actual = registrationService.apply(accountId, request)
                actual shouldBe registration.id
                verify { registrationRepository.save(any()) }
            }
        }
    })
