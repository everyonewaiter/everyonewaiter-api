package com.everyonewaiter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

abstract class EveryonewaiterTestContainers {

  private static final String MYSQL_IMAGE_NAME = "mysql:8.4.3";
  private static final String MYSQL_DATABASE_NAME = "everyonewaiter";
  private static final String MYSQL_USERNAME = "root";
  private static final String MYSQL_PASSWORD = "1234";

  private static final String REDIS_IMAGE_NAME = "redis:7.4.1-alpine";
  private static final int REDIS_PORT = 6379;

  private static final MySQLContainer<?> MYSQL_CONTAINER;
  private static final GenericContainer<?> REDIS_CONTAINER;

  static {
    try (var container = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE_NAME))) {
      MYSQL_CONTAINER = container.withDatabaseName(MYSQL_DATABASE_NAME)
          .withUsername(MYSQL_USERNAME)
          .withPassword(MYSQL_PASSWORD)
          .withUrlParam("rewriteBatchedStatements", "true")
          .withUrlParam("characterEncoding", "UTF-8");
    }

    try (var container = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME))) {
      REDIS_CONTAINER = container.withExposedPorts(REDIS_PORT);
    }
  }

  @BeforeAll
  static void startContainers() {
    MYSQL_CONTAINER.start();
    REDIS_CONTAINER.start();
  }

  @AfterAll
  static void stopContainers() {
    MYSQL_CONTAINER.stop();
    REDIS_CONTAINER.stop();
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);

    registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", EveryonewaiterTestContainers::getRedisContainerPort);
  }

  private static String getRedisContainerPort() {
    return REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString();
  }

}
