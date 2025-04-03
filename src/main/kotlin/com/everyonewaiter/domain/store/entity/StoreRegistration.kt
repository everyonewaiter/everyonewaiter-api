package com.everyonewaiter.domain.store.entity

import com.everyonewaiter.domain.store.event.StoreRegistrationApplyEvent
import com.everyonewaiter.global.entity.AggregateRootEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
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
    var imageId: Long,
    var name: String,
    var ceoName: String,
    var address: String,
    var landline: String,
    var license: String,
    var status: StoreRegistrationStatus = StoreRegistrationStatus.APPLY,
    var reason: String = "",
) : AggregateRootEntity() {
    companion object {
        fun create(
            accountId: Long,
            imageId: Long,
            name: String,
            ceoName: String,
            address: String,
            landline: String,
            license: String,
        ): StoreRegistration {
            val registration = StoreRegistration(
                accountId = accountId,
                imageId = imageId,
                name = name,
                ceoName = ceoName,
                address = address,
                landline = landline,
                license = license,
            )
            registration.registerEvent(StoreRegistrationApplyEvent(name))
            return registration
        }
    }
}
