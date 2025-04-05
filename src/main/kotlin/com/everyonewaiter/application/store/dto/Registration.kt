package com.everyonewaiter.application.store.dto

import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.entity.StoreRegistrationStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

class Registration {
    data class PageRequest(
        val page: Long = 1L,
        val size: Long = 20L,
    )

    @Schema(name = "Registrations.PageResponse")
    data class PageResponse(
        val registrations: List<Response>,
        @Schema(description = "매장 등록 신청 수", example = "201")
        val registrationCount: Long,
    ) {
        companion object {
            fun of(
                registrations: List<StoreRegistration>,
                registrationCount: Long,
            ) = PageResponse(
                registrations.map { Response.from(it) },
                registrationCount = registrationCount,
            )
        }
    }

    @Schema(name = "Registrations.Response")
    data class Response(
        @Schema(description = "매장 등록 ID", example = "\"694865267482835533\"")
        val registrationId: String,
        @Schema(description = "계정 ID", example = "\"694865267482835533\"")
        val accountId: String,
        @Schema(description = "매장 이름", example = "홍길동식당")
        val name: String,
        @Schema(description = "대표자명", example = "홍길동")
        val ceoName: String,
        @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123")
        val address: String,
        @Schema(description = "매장 전화번호", example = "02-123-4567")
        val landline: String,
        @Schema(description = "사업자 등록번호", example = "443-60-00875")
        val license: String,
        @Schema(description = "사업자 등록증 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
        val image: String,
        @Schema(description = "매장 등록 신청 상태", example = "APPLY")
        val status: StoreRegistrationStatus,
        @Schema(description = "매장 등록 거부 사유", example = "사업자 정보를 조회할 수 없습니다.")
        val reason: String,
        @Schema(description = "매장 등록 신청일", example = "2025-01-01 12:00:00")
        val createdAt: Instant,
        @Schema(description = "매장 등록 신청 수정일", example = "2025-01-01 12:00:00")
        val updatedAt: Instant,
    ) {
        companion object {
            fun from(registration: StoreRegistration) =
                Response(
                    registrationId = registration.id.toString(),
                    accountId = registration.accountId.toString(),
                    name = registration.licenseInformation.name,
                    ceoName = registration.licenseInformation.ceoName,
                    address = registration.licenseInformation.address,
                    landline = registration.licenseInformation.landline,
                    license = registration.licenseInformation.license,
                    image = registration.licenseInformation.image,
                    status = registration.status,
                    reason = registration.reason,
                    createdAt = registration.createdAt,
                    updatedAt = registration.updatedAt,
                )
        }
    }
}
