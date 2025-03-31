package com.everyonewaiter.domain.auth.repository

import com.everyonewaiter.domain.auth.entity.AuthCode

fun interface AuthCodeRepository {
    fun save(authCode: AuthCode)
}
