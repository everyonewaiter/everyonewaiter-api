package com.everyonewaiter.infrastructure.auth

import com.everyonewaiter.domain.auth.entity.AuthCode
import com.everyonewaiter.domain.auth.repository.AuthCodeRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AuthCodeRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) : AuthCodeRepository {
    override fun save(authCode: AuthCode) {
        redisTemplate.opsForValue()[authCode.key, authCode.value] = authCode.expiration
    }
}
