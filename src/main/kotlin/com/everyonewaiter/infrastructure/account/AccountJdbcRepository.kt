package com.everyonewaiter.infrastructure.account

import com.everyonewaiter.domain.account.entity.Account
import org.springframework.data.repository.CrudRepository

interface AccountJdbcRepository : CrudRepository<Account, Long> {
    fun existsByEmail(email: String): Boolean

    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun findByEmail(email: String): Account?
}
