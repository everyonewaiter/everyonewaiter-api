package com.everyonewaiter.domain.order;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.shared.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class OrderOption {

  private String name;

  private long price;

  private Position position;

  public static OrderOption create(String name, long price, Position menuOptionPosition) {
    OrderOption orderOption = new OrderOption();

    orderOption.name = name;
    orderOption.price = price;
    orderOption.position = Position.copy(menuOptionPosition);

    return orderOption;
  }

}
