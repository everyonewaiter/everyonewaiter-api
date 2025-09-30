package com.everyonewaiter;

import static com.everyonewaiter.TestContainerCreator.REDIS_PASSWORD;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
class TestContainerSupport {

  static final MySQLContainer<?> MYSQL_CONTAINER = TestContainerCreator.createMysqlContainer();
  static final GenericContainer<?> REDIS_CONTAINER = TestContainerCreator.createRedisContainer();

  static {
    MYSQL_CONTAINER.start();
    System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
    System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
    System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

    REDIS_CONTAINER.start();
    System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
    System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getFirstMappedPort().toString());
    System.setProperty("spring.data.redis.password", REDIS_PASSWORD);
  }

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
