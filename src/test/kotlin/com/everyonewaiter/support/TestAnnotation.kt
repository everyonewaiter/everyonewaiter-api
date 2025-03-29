package com.everyonewaiter.support

import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataRedisTest
@Import(TestRedisContainerConfiguration::class)
annotation class RedisTest

@Suppress("unused")
@ActiveProfiles("test")
@DataJdbcTest
@Import(TestMysqlContainerConfiguration::class)
annotation class MySQLTest
