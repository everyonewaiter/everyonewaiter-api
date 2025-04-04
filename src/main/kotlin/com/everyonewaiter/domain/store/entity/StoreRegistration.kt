package com.everyonewaiter.domain.store.entity

import com.everyonewaiter.domain.store.event.StoreRegistrationApplyEvent
import com.everyonewaiter.global.entity.AggregateRootEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("store_registration")
data class StoreRegistration(
    @Id
    @Column("registration_id")
    override var id: Long = 0L,
    override val createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = createdAt,
    val accountId: Long,
    @Embedded.Empty
    val licenseInformation: BusinessLicenseInformation,
    var status: StoreRegistrationStatus = StoreRegistrationStatus.APPLY,
    var reason: String = "",
) : AggregateRootEntity() {
    companion object {
        fun create(
            accountId: Long,
            licenseInformation: BusinessLicenseInformation,
        ): StoreRegistration {
            val registration = StoreRegistration(
                accountId = accountId,
                licenseInformation = licenseInformation,
            )
            registration.registerEvent(StoreRegistrationApplyEvent(licenseInformation.name))
            return registration
        }
    }
}
