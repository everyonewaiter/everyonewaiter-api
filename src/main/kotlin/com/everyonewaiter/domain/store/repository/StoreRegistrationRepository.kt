package com.everyonewaiter.domain.store.repository

import com.everyonewaiter.domain.store.entity.StoreRegistration

interface StoreRegistrationRepository {
    fun count(
        accountId: Long,
        limit: Long,
    ): Long

    fun findAll(
        accountId: Long,
        limit: Long,
        offset: Long,
    ): List<StoreRegistration>

    fun findOneOrThrow(
        registrationId: Long,
        accountId: Long,
    ): StoreRegistration

    fun save(registration: StoreRegistration): StoreRegistration
}
