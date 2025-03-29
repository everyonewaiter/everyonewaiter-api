package com.everyonewaiter.support

import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataRedisTest
@Import(TestRedisContainerConfiguration::class)
annotation class RedisTest

@ActiveProfiles("test")
@SpringBootTest
@Import(value = [TestMysqlContainerConfiguration::class, TestRedisContainerConfiguration::class])
annotation class IntegrationTest
