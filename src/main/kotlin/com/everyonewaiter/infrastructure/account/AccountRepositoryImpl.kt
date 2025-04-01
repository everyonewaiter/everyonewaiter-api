package com.everyonewaiter.infrastructure.account

import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.repository.AccountRepository
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryImpl(
    private val accountJdbcRepository: AccountJdbcRepository,
) : AccountRepository {
    override fun existsByEmail(email: String): Boolean = accountJdbcRepository.existsByEmail(email)

    override fun existsByPhone(phoneNumber: String): Boolean = accountJdbcRepository.existsByPhoneNumber(phoneNumber)

    override fun findByEmail(email: String): Account? = accountJdbcRepository.findByEmail(email)

    override fun save(account: Account): Account = accountJdbcRepository.save(account)
}
