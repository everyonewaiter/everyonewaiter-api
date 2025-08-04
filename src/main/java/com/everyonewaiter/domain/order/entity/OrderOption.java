package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.shared.Position;
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

  public static OrderOption create(String name, long price, Position menuOptionPosition) {
    OrderOption orderOption = new OrderOption();
    orderOption.name = name;
    orderOption.price = price;
    orderOption.position = Position.copy(menuOptionPosition);
    return orderOption;
  }

}
