package com.everyonewaiter.domain.auth.repository

import com.everyonewaiter.domain.auth.entity.AuthSuccess

interface AuthSuccessRepository {
    fun exists(authSuccess: AuthSuccess): Boolean

    fun save(authSuccess: AuthSuccess)
}
