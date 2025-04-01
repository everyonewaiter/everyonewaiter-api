package com.everyonewaiter.global.entity

import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.Instant

abstract class AggregateRootEntity : AbstractAggregateRoot<AggregateRootEntity>() {
    abstract var id: Long
    abstract val createdAt: Instant

    @LastModifiedDate
    open val updatedAt: Instant = Instant.now()

    val domainEvents: List<Any>
        get() = domainEvents().toList()
}
