package com.everyonewaiter.domain.waiting;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CustomerCall {

  private static final int MAX_CALL_COUNT = 5;

  private int count;

  private Instant lastCallTime;

  protected CustomerCall() {
    this.count = 0;
    this.lastCallTime = Instant.ofEpochMilli(0L);
  }

  public void call() {
    if (this.count >= MAX_CALL_COUNT) {
      throw new ExceedMaxCustomerCallException();
    }

    this.count++;
    this.lastCallTime = Instant.now();
  }

}
