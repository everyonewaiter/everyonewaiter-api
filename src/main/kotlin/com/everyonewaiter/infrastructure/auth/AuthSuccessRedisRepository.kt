package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthSuccess
import com.everyonewaiter.domain.auth.repository.AuthSuccessRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AuthSuccessRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) : AuthSuccessRepository {
    override fun exists(authSuccess: AuthSuccess): Boolean = redisTemplate.opsForValue()[authSuccess.key].isNullOrBlank().not()

    override fun save(authSuccess: AuthSuccess) {
        redisTemplate.opsForValue()[authSuccess.key, authSuccess.value] = authSuccess.expiration
    }
}
