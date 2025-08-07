package com.everyonewaiter.domain.waiting.entity;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerCallCount {

  private static final int MAX_CALL_COUNT = 5;

  @Column(name = "call_count", nullable = false)
  private int value;

  @Column(name = "last_call_time", nullable = false)
  private Instant lastCallTime;

  public CustomerCallCount(int value, Instant lastCallTime) {
    this.value = value;
    this.lastCallTime = lastCallTime;
  }

  public void increase() {
    if (this.value < MAX_CALL_COUNT) {
      this.value++;
      this.lastCallTime = Instant.now();
    } else {
      throw new BusinessException(ErrorCode.EXCEED_MAXIMUM_CUSTOMER_CALL_COUNT);
    }
  }

}
