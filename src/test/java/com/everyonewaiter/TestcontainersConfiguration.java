package com.everyonewaiter;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

  private static final String REDISSON_HOST_PREFIX = "redis://";

  @Bean
  @ServiceConnection
  MySQLContainer<?> mysqlContainer() {
    return new MySQLContainer<>(DockerImageName.parse("mysql:8.4.3"));
  }

  @Bean
  @ServiceConnection(name = "redis")
  GenericContainer<?> redisContainer() {
    try (
        GenericContainer<?> container =
            new GenericContainer<>(DockerImageName.parse("redis:7.4.1-alpine"))
    ) {
      return container.withExposedPorts(6379);
    }
  }

  @Bean
  @Profile("test")
  public RedissonClient redissonClient(GenericContainer<?> redisContainer) {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(
            REDISSON_HOST_PREFIX
                + redisContainer.getHost()
                + ":"
                + redisContainer.getMappedPort(6379)
        );
    return Redisson.create(config);
  }

}
