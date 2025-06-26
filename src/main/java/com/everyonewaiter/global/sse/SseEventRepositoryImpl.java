package com.everyonewaiter.global.sse;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class SseEventRepositoryImpl implements SseEventRepository {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void save(String key, String event) {
    redisTemplate.opsForValue().set(key, event, Duration.ofMinutes(10));
  }

  @Override
  public Map<String, String> findAllByScanKey(String scanKey) {
    ScanOptions scanOptions = ScanOptions.scanOptions()
        .match(scanKey + "*")
        .count(100)
        .build();

    try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
      Map<String, String> events = new HashMap<>();

      while (cursor.hasNext()) {
        String key = cursor.next();
        String event = redisTemplate.opsForValue().get(key);
        events.put(key, event);
      }

      return events;
    }
  }

}
