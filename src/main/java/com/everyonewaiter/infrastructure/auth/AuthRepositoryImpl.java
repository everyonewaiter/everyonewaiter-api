package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.application.auth.required.AuthRepository;
import com.everyonewaiter.domain.auth.Auth;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AuthRepositoryImpl implements AuthRepository {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public boolean exists(Auth auth) {
    return Objects.requireNonNullElse(redisTemplate.hasKey(auth.key()), false);
  }

  @Override
  public int find(Auth auth) {
    String value = Objects.requireNonNullElse(redisTemplate.opsForValue().get(auth.key()), "0");
    return Integer.parseInt(value);
  }

  @Override
  public void save(Auth auth) {
    redisTemplate.opsForValue()
        .set(auth.key(), String.valueOf(auth.value()), auth.expiration());
  }

  @Override
  public void increment(Auth auth) {
    Long incrementedValue = redisTemplate.opsForValue().increment(auth.key());
    if (incrementedValue != null && incrementedValue == 1L) {
      redisTemplate.expire(auth.key(), auth.expiration());
    }
  }

  @Override
  public void delete(Auth auth) {
    redisTemplate.delete(auth.key());
  }

}
