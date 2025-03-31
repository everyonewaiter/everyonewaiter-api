package com.everyonewaiter.domain.auth.repository

import com.everyonewaiter.domain.auth.entity.AuthAttempt

interface AuthAttemptRepository {
    fun find(authAttempt: AuthAttempt): Int

    fun increment(authAttempt: AuthAttempt)

    fun save(authAttempt: AuthAttempt)
}
