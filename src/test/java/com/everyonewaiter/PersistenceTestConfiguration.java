package com.everyonewaiter;

import static com.everyonewaiter.TestContainerCreator.REDIS_PORT;

import lombok.EqualsAndHashCode;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration
class PersistenceTestConfiguration {

  private static final String REDISSON_HOST_PREFIX = "redis://";

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(GenericContainer<?> redisContainer) {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

    configuration.setHostName(redisContainer.getHost());
    configuration.setPort(redisContainer.getMappedPort(REDIS_PORT));

    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }

  @Bean
  public RedissonClient redissonClient(GenericContainer<?> redisContainer) {
    Config config = new Config();

    config.useSingleServer()
        .setAddress(
            REDISSON_HOST_PREFIX
                + redisContainer.getHost()
                + ":"
                + redisContainer.getMappedPort(REDIS_PORT)
        );

    return Redisson.create(config);
  }

  @EqualsAndHashCode(callSuper = false)
  static class ExcludeRedisConfiguration extends TypeExcludeFilter {

    @Override
    public boolean match(
        MetadataReader metadataReader,
        MetadataReaderFactory metadataReaderFactory
    ) {
      return metadataReader
          .getClassMetadata()
          .getClassName()
          .equals("com.everyonewaiter.adapter.persistence.RedisConfiguration");
    }

  }

}
