package com.everyonewaiter.infrastructure.store

import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.domain.store.repository.StoreRegistrationRepository
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.navercorp.spring.data.jdbc.plus.sql.provider.EntityJdbcProvider
import com.navercorp.spring.data.jdbc.plus.sql.support.JdbcRepositorySupport
import com.navercorp.spring.data.jdbc.plus.sql.support.trait.SingleValueSelectTrait
import org.springframework.stereotype.Repository

@Repository
class StoreRegistrationRepositoryImpl(
    entityJdbcProvider: EntityJdbcProvider,
    private val registrationJdbcRepository: StoreRegistrationJdbcRepository,
) : JdbcRepositorySupport<StoreRegistration>(StoreRegistration::class.java, entityJdbcProvider),
    SingleValueSelectTrait,
    StoreRegistrationRepository {
    val sql: StoreRegistrationSql = super.sqls(::StoreRegistrationSql)

    override fun count(
        accountId: Long,
        limit: Long,
    ): Long {
        val parameterSource = mapParameterSource()
            .addValue("accountId", accountId)
            .addValue("limit", limit)
        return selectSingleValue(sql.countByAccountIdAndPaging(), parameterSource, Long::class.java)
    }

    override fun findAll(
        accountId: Long,
        limit: Long,
        offset: Long,
    ): List<StoreRegistration> {
        val parameterSource = mapParameterSource()
            .addValue("accountId", accountId)
            .addValue("limit", limit)
            .addValue("offset", offset)
        return find(sql.selectByAccountIdAndPaging(), parameterSource)
    }
    override fun save(registration: StoreRegistration): StoreRegistration = registrationJdbcRepository.save(registration)
}
