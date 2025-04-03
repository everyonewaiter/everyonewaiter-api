package com.everyonewaiter.domain.image.entity

import com.everyonewaiter.global.entity.AggregateRootEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.Date

@Table("image")
data class Image(
    @Id
    @Column("image_id")
    override var id: Long = 0L,
    override val createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = createdAt,
    val accountId: Long,
    val name: String,
    val originalName: String,
    var state: ImageState = ImageState.ACTIVE,
    var accessUrl: String = "",
    var accessUrlExpiration: Date = Date.from(Instant.ofEpochMilli(0L)),
) : AggregateRootEntity() {
    companion object {
        fun create(
            accountId: Long,
            name: String,
            originalName: String,
        ) = Image(
            accountId = accountId,
            name = name,
            originalName = originalName,
        )
    }
}
