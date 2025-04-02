package com.everyonewaiter.global.config

import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.global.entity.AggregateRootEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.relational.core.conversion.MutableAggregateChange
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback
import java.time.Instant

@Configuration
class JdbcConfiguration {
    @Bean
    fun aggregateRootEntityCallback(): AggregateRootEntityCallback = AggregateRootEntityCallback()

    @Order(Ordered.HIGHEST_PRECEDENCE)
    class AggregateRootEntityCallback :
        BeforeConvertCallback<AggregateRootEntity>,
        BeforeSaveCallback<AggregateRootEntity> {
        override fun onBeforeConvert(aggregate: AggregateRootEntity): AggregateRootEntity =
            aggregate.apply { if (id == 0L) id = Tsid.nextLong() }

        override fun onBeforeSave(
            aggregate: AggregateRootEntity,
            aggregateChange: MutableAggregateChange<AggregateRootEntity>,
        ): AggregateRootEntity = aggregate.apply { updatedAt = Instant.now() }
    }
}
