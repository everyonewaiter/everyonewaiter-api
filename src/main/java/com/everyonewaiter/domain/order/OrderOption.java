package com.everyonewaiter.domain.order;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.menu.MenuOption;
import com.everyonewaiter.domain.menu.MenuOptionGroup;
import com.everyonewaiter.domain.shared.Position;
import java.util.List;
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

  public static OrderOption create(
      MenuOptionGroup menuOptionGroup,
      OrderOptionModifyRequest request
  ) {
    MenuOption menuOption = menuOptionGroup.getMenuOption(request.name(), request.price());

    OrderOption orderOption = new OrderOption();

    orderOption.name = requireNonNull(menuOption.getName());
    orderOption.price = menuOption.getPrice();
    orderOption.position = Position.copy(menuOption.getPosition());

    return orderOption;
  }

  public static List<OrderOption> createOrderOptions(
      MenuOptionGroup menuOptionGroup,
      List<OrderOptionModifyRequest> requests
  ) {
    return requests.stream()
        .map(request -> create(menuOptionGroup, request))
        .toList();
  }

}
