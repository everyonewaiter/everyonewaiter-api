package com.everyonewaiter.global.entity

import org.springframework.data.domain.AbstractAggregateRoot
import java.time.Instant

abstract class AggregateRootEntity : AbstractAggregateRoot<AggregateRootEntity>() {
    abstract var id: Long
    abstract val createdAt: Instant
    abstract var updatedAt: Instant

    val domainEvents: List<Any>
        get() = domainEvents().toList()
}
