package com.everyonewaiter.domain.order.entity;

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
public class Serving {

  @Column(name = "served", nullable = false)
  private boolean served = false;

  @Column(name = "served_time", nullable = false)
  private Instant servedTime = Instant.ofEpochMilli(0L);

}
