package com.everyonewaiter.adapter.persistence.receipt;

import static java.util.Objects.requireNonNullElse;

import com.everyonewaiter.application.receipt.required.ReceiptRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ReceiptRedisRepository implements ReceiptRepository {

  private static final String PRINT_NO_KEY_PREFIX = "print_no:";

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public int getPrintNo(Long storeId) {
    String key = PRINT_NO_KEY_PREFIX + storeId;

    String value = requireNonNullElse(redisTemplate.opsForValue().get(key), "1");

    return Integer.parseInt(value);
  }

  @Override
  public void incrementPrintNo(Long storeId) {
    String key = PRINT_NO_KEY_PREFIX + storeId;

    Long incrementedValue = redisTemplate.opsForValue().increment(key);

    if (incrementedValue != null && incrementedValue == 1L) {
      redisTemplate.expire(key, Duration.ofHours(24));
    }
  }

  @Override
  public void deletePrintNo(Long storeId) {
    String key = PRINT_NO_KEY_PREFIX + storeId;

    redisTemplate.delete(key);
  }

}
