package com.everyonewaiter.domain.order;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
public class Serving {

  private boolean served;

  private Instant servedTime;

  protected Serving() {
    this.served = false;
    this.servedTime = Instant.ofEpochMilli(0L);
  }

  public void complete() {
    if (!isServed()) {
      this.served = true;
      this.servedTime = Instant.now();
    }
  }

  public void cancel() {
    if (isServed()) {
      this.served = false;
      this.servedTime = Instant.ofEpochSecond(0L);
    }
  }

}
