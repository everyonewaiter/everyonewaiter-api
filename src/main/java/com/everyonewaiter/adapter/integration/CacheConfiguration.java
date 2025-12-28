package com.everyonewaiter.adapter.integration;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableCaching
class CacheConfiguration {

  @Bean
  public CacheManager cacheManager(
      LettuceConnectionFactory connectionFactory,
      RedisCacheConfiguration redisCacheConfiguration
  ) {
    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(connectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration(JsonMapper jsonMapper) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer())
        )
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJacksonJsonRedisSerializer(jsonMapper))
        )
        .entryTtl(Duration.ofHours(3));
  }

}
