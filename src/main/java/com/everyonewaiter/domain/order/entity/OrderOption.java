package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
public class OrderOption {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Embedded
  private Position position;

}
