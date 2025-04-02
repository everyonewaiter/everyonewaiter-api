package com.everyonewaiter.domain.account.entity

import com.everyonewaiter.domain.account.event.AccountCreateEvent
import com.everyonewaiter.global.entity.AggregateRootEntity
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("account")
data class Account(
    @Id
    @Column("account_id")
    override var id: Long = 0L,
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = createdAt,
    val email: String,
    var password: String,
    var phoneNumber: String,
    var permission: AccountPermission = AccountPermission.USER,
    var status: AccountStatus = AccountStatus.INACTIVE,
    var lastSignIn: Instant = Instant.ofEpochMilli(0L),
) : AggregateRootEntity() {
    val isInactive: Boolean
        get() = status == AccountStatus.INACTIVE

    val isActive: Boolean
        get() = status == AccountStatus.ACTIVE

    fun signIn() {
        lastSignIn = Instant.now()
    }

    fun activate() {
        checkOrThrow(isInactive, ErrorCode.ALREADY_VERIFIED_EMAIL)
        status = AccountStatus.ACTIVE
    }

    companion object {
        fun create(
            email: String,
            password: String,
            phoneNumber: String,
        ): Account {
            val account = Account(email = email, password = password, phoneNumber = phoneNumber)
            account.registerEvent(AccountCreateEvent(email))
            return account
        }
    }
}
