package com.everyonewaiter.domain.account.repository

import com.everyonewaiter.domain.account.entity.Account

interface AccountRepository {
    fun existsByEmail(email: String): Boolean

    fun existsByPhone(phoneNumber: String): Boolean

    fun save(account: Account): Account
}
