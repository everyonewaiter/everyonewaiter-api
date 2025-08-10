package com.everyonewaiter;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

class TestContainerCreator {

  static final String MYSQL_IMAGE_NAME = "mysql:8.4.3";
  static final String MYSQL_DATABASE_NAME = "everyonewaiter";
  static final String MYSQL_USERNAME = "root";
  static final String MYSQL_PASSWORD = "1234";

  static final String REDIS_IMAGE_NAME = "redis:7.4.1-alpine";
  static final int REDIS_PORT = 6379;

  static MySQLContainer<?> createMysqlContainer() {
    try (var container = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE_NAME))) {
      return container.withDatabaseName(MYSQL_DATABASE_NAME)
          .withUsername(MYSQL_USERNAME)
          .withPassword(MYSQL_PASSWORD)
          .withUrlParam("rewriteBatchedStatements", "true")
          .withUrlParam("characterEncoding", "UTF-8");
    }
  }

  static GenericContainer<?> createRedisContainer() {
    try (var container = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME))) {
      return container.withExposedPorts(REDIS_PORT);
    }
  }

}
