package com.everyonewaiter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
class TestContainerSupport {

  static final MySQLContainer<?> MYSQL_CONTAINER = TestContainerCreator.createMysqlContainer();
  static final GenericContainer<?> REDIS_CONTAINER = TestContainerCreator.createRedisContainer();

  @Bean
  @ServiceConnection
  MySQLContainer<?> mysqlContainer() {
    return MYSQL_CONTAINER;
  }

  @Bean
  @ServiceConnection(name = "redis")
  GenericContainer<?> redisContainer() {
    return REDIS_CONTAINER;
  }

}
