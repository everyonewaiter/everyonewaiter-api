package com.everyonewaiter.global.annotation

import com.everyonewaiter.domain.account.entity.AccountPermission

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticationAccount(
    val permission: AccountPermission = AccountPermission.USER,
)
