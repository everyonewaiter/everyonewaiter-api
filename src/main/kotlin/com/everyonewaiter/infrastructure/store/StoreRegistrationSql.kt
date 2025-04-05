package com.everyonewaiter.infrastructure.store

import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.navercorp.spring.data.jdbc.plus.sql.support.SqlGeneratorSupport

class StoreRegistrationSql : SqlGeneratorSupport() {
    fun countByAccountIdAndPaging(): String =
        """
        select count(*)
        from (
         select registration_id
         from ${sql.tables(StoreRegistration::class.java)}
         where account_id = :accountId
         limit :limit
        ) as t
        """.trimIndent()

    fun selectByAccountIdAndPaging(): String =
        """
        select ${sql.columns(StoreRegistration::class.java)}
        from (
         select registration_id 
         from ${sql.tables(StoreRegistration::class.java)}
         where account_id = :accountId
         order by registration_id desc
         limit :limit
         offset :offset
        ) as t
        left outer join ${sql.tables(StoreRegistration::class.java)}
        on t.registration_id = ${sql.tables(StoreRegistration::class.java)}.registration_id
        """.trimIndent()
}
