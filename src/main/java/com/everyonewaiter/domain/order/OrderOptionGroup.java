package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.order.OrderOption.createOrderOptions;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuOptionGroup;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"orderMenu", "orderOptions"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderOptionGroup extends AggregateEntity {

  private OrderMenu orderMenu;

  private String name;

  private boolean printEnabled;

  private List<OrderOption> orderOptions = new ArrayList<>();

  public static OrderOptionGroup create(
      Menu menu,
      OrderMenu orderMenu,
      OrderOptionGroupModifyRequest request
  ) {
    MenuOptionGroup menuOptionGroup = menu.getMenuOptionGroup(request.menuOptionGroupId());

    OrderOptionGroup orderOptionGroup = new OrderOptionGroup();

    orderOptionGroup.orderMenu = requireNonNull(orderMenu);
    orderOptionGroup.name = requireNonNull(menuOptionGroup.getName());
    orderOptionGroup.printEnabled = menuOptionGroup.isPrintEnabled();
    orderOptionGroup.orderOptions.addAll(
        createOrderOptions(menuOptionGroup, request.orderOptions())
    );

    return orderOptionGroup;
  }

  public static List<OrderOptionGroup> createOrderOptionGroups(
      Menu menu,
      OrderMenu orderMenu,
      List<OrderOptionGroupModifyRequest> requests
  ) {
    return requests.stream()
        .map(request -> create(menu, orderMenu, request))
        .toList();
  }

  public long calculateTotalPrice() {
    return getOrderOptions().stream()
        .mapToLong(OrderOption::getPrice)
        .sum();
  }

  public List<OrderOption> getOrderOptions() {
    return Collections.unmodifiableList(orderOptions);
  }

  public List<String> getFormattedOrderOptions() {
    return getOrderOptions().stream()
        .map(option -> "- %s: %s".formatted(name, option.getName()))
        .toList();
  }

}
