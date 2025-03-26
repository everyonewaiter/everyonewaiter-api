package com.everyonewaiter.support

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestMysqlContainerConfiguration {
    @Bean
    @ServiceConnection
    fun mysqlContainer(): MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.4.3"))
}

@TestConfiguration(proxyBeanMethods = false)
class TestRedisContainerConfiguration {
    @Bean
    @ServiceConnection(name = "redis")
    fun redisContainer(): GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.4.1-alpine")).withExposedPorts(6379)
}
