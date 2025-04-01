package com.everyonewaiter.domain.auth.repository

import com.everyonewaiter.domain.auth.entity.AuthCode

interface AuthCodeRepository {
    fun find(authCode: AuthCode): Int?

    fun save(authCode: AuthCode)
}
