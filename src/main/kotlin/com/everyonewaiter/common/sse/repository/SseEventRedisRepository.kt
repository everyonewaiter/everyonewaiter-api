package com.everyonewaiter.common.sse.repository

import com.everyonewaiter.common.sse.SseEventRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
internal class SseEventRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) : SseEventRepository {
    override fun save(
        key: String,
        event: String,
    ) {
        redisTemplate.opsForValue()[key, event] = Duration.ofMinutes(10)
    }

    override fun findAllByScanKey(scanKey: String): Map<String, String> {
        val scanOptions = ScanOptions
            .scanOptions()
            .match("$scanKey*")
            .count(20)
            .build()
        redisTemplate.scan(scanOptions).use { cursor ->
            val result = mutableMapOf<String, String>()
            cursor.forEach { result[it] = redisTemplate.opsForValue()[it]!! }
            return result
        }
    }

    override fun delete(key: String) {
        redisTemplate.delete(key)
    }
}
