package com.everyonewaiter.global.config;

import com.everyonewaiter.global.support.ProfileChecker;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
class RedisConfiguration {

  private static final String REDISSON_HOST_PREFIX = "redis://";

  private final RedisProperties redisProperties;
  private final ProfileChecker profileChecker;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisProperties.getHost());
    configuration.setPort(redisProperties.getPort());
    configuration.setPassword(redisProperties.getPassword());
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectionFactory) {
    StringRedisTemplate redisTemplate = new StringRedisTemplate(connectionFactory);
    redisTemplate.setEnableTransactionSupport(true);
    return redisTemplate;
  }

  @Bean
  public RedissonClient redissonClient() {
    boolean usePassword = profileChecker.isProduction() || profileChecker.isDevelopment();
    Config config = new Config();
    config.useSingleServer()
        .setAddress(
            REDISSON_HOST_PREFIX
                + redisProperties.getHost()
                + ":"
                + redisProperties.getPort()
        )
        .setPassword(usePassword ? redisProperties.getPassword() : null);
    return Redisson.create(config);
  }

}
