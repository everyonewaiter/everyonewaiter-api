package com.everyonewaiter.support

import com.everyonewaiter.global.config.JdbcConfiguration
import com.navercorp.spring.boot.autoconfigure.data.jdbc.plus.sql.JdbcPlusSqlAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataRedisTest
@Import(TestRedisContainerConfiguration::class)
annotation class RedisTest

@ActiveProfiles("test")
@DataJdbcTest
@ImportAutoConfiguration(classes = [JdbcConfiguration::class, JdbcPlusSqlAutoConfiguration::class])
@Import(TestMysqlContainerConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
annotation class MysqlTest

@ActiveProfiles("test")
@SpringBootTest
@Import(value = [TestMysqlContainerConfiguration::class, TestRedisContainerConfiguration::class])
annotation class IntegrationTest
