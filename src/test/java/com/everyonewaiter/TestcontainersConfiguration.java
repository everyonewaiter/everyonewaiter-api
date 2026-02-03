package com.everyonewaiter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

  private static final String REDIS_PASSWORD = "1234";

  @Bean
  MySQLContainer mysqlContainer() {
    return new MySQLContainer(DockerImageName.parse("mysql:8.4.3"))
        .withDatabaseName("everyonewaiter")
        .withUsername("root")
        .withPassword("1234")
        .withUrlParam("rewriteBatchedStatements", "true")
        .withUrlParam("characterEncoding", "UTF-8");
  }

  @Bean
  GenericContainer<?> redisContainer() {
    return new GenericContainer<>(DockerImageName.parse("redis:7.4.1"))
        .withCommand("redis-server", "--requirepass", REDIS_PASSWORD)
        .withExposedPorts(6379);
  }

  @Bean
  DynamicPropertyRegistrar dynamicPropertyRegistrar(
      MySQLContainer mysqlContainer,
      GenericContainer<?> redisContainer
  ) {
    return registry -> {
      registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
      registry.add("spring.datasource.username", mysqlContainer::getUsername);
      registry.add("spring.datasource.password", mysqlContainer::getPassword);

      registry.add("spring.data.redis.host", redisContainer::getHost);
      registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
      registry.add("spring.data.redis.password", () -> REDIS_PASSWORD);
    };
  }

}
