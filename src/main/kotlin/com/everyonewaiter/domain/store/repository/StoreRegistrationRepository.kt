package com.everyonewaiter.domain.store.repository

import com.everyonewaiter.domain.store.entity.StoreRegistration

fun interface StoreRegistrationRepository {
    fun save(registration: StoreRegistration): StoreRegistration
}
