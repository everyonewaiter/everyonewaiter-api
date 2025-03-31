package com.everyonewaiter.global.config

import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.global.entity.AggregateRootEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback

@Configuration
@EnableJdbcAuditing
class JdbcConfiguration {
    @Bean
    fun aggregateRootEntityCallback(): AggregateRootEntityCallback = AggregateRootEntityCallback()

    @Order(Ordered.HIGHEST_PRECEDENCE)
    class AggregateRootEntityCallback : BeforeConvertCallback<AggregateRootEntity> {
        override fun onBeforeConvert(aggregate: AggregateRootEntity): AggregateRootEntity {
            if (aggregate.id == 0L) {
                aggregate.id = Tsid.nextLong()
            }
            return aggregate
        }
    }
}
