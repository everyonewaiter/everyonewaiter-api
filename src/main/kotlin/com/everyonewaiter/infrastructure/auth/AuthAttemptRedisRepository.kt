package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthAttempt
import com.everyonewaiter.domain.auth.repository.AuthAttemptRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AuthAttemptRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) : AuthAttemptRepository {
    override fun find(authAttempt: AuthAttempt): Int = redisTemplate.opsForValue()[authAttempt.key]?.toInt() ?: 0

    override fun increment(authAttempt: AuthAttempt) {
        val attemptCount = redisTemplate.opsForValue().increment(authAttempt.key)
        if (attemptCount != null && attemptCount == 1L) {
            redisTemplate.expire(authAttempt.key, authAttempt.expiration)
        }
    }

    override fun save(authAttempt: AuthAttempt) {
        redisTemplate.opsForValue()[authAttempt.key, authAttempt.value] = authAttempt.expiration
    }
}
