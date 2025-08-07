package com.everyonewaiter;

import static com.everyonewaiter.TestContainerCreator.REDIS_PORT;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class EveryonewaiterTestConfiguration {

  private static final MySQLContainer<?> MYSQL_CONTAINER = TestContainerCreator.createMysqlContainer();
  private static final GenericContainer<?> REDIS_CONTAINER = TestContainerCreator.createRedisContainer();

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

  @DynamicPropertySource
  static void registryProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);

    registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", EveryonewaiterTestConfiguration::getRedisContainerPort);
  }

  private static String getRedisContainerPort() {
    return REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString();
  }

}
