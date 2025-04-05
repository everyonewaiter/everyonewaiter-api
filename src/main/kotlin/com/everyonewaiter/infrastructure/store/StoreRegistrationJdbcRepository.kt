package com.everyonewaiter.infrastructure.store

import com.everyonewaiter.domain.store.entity.StoreRegistration
import org.springframework.data.repository.CrudRepository

interface StoreRegistrationJdbcRepository : CrudRepository<StoreRegistration, Long> {
    fun findByIdAndAccountId(
        id: Long,
        accountId: Long,
    ): StoreRegistration?
}
