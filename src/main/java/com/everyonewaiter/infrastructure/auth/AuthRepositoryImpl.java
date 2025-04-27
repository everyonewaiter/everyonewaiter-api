package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.domain.auth.entity.Auth;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
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
    return Objects.requireNonNullElse(redisTemplate.hasKey(auth.getKey()), false);
  }

  @Override
  public int find(Auth auth) {
    String value = Objects.requireNonNullElse(redisTemplate.opsForValue().get(auth.getKey()), "0");
    return Integer.parseInt(value);
  }

  @Override
  public void save(Auth auth) {
    redisTemplate.opsForValue()
        .set(auth.getKey(), String.valueOf(auth.getValue()), auth.expiration());
  }

  @Override
  public void increment(Auth auth) {
    Long incrementedValue = redisTemplate.opsForValue().increment(auth.getKey());
    if (incrementedValue != null && incrementedValue == 1L) {
      redisTemplate.expire(auth.getKey(), auth.expiration());
    }
  }

  @Override
  public void delete(Auth auth) {
    redisTemplate.delete(auth.getKey());
  }

}
