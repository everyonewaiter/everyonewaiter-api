package com.everyonewaiter.infrastructure.store

import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.repository.StoreRegistrationRepository
import org.springframework.stereotype.Repository

@Repository
class StoreRegistrationRepositoryImpl(
    private val registrationJdbcRepository: StoreRegistrationJdbcRepository,
) : StoreRegistrationRepository {
    override fun save(registration: StoreRegistration): StoreRegistration = registrationJdbcRepository.save(registration)
}
