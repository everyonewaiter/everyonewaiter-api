package com.everyonewaiter.domain.order;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
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

  public static OrderOptionGroup create(OrderMenu orderMenu, String name, boolean printEnabled) {
    OrderOptionGroup orderOptionGroup = new OrderOptionGroup();

    orderOptionGroup.orderMenu = orderMenu;
    orderOptionGroup.name = name;
    orderOptionGroup.printEnabled = printEnabled;

    return orderOptionGroup;
  }

  public void addOrderOption(OrderOption orderOption) {
    orderOptions.add(orderOption);
  }

  public List<OrderOption> getOrderOptions() {
    return Collections.unmodifiableList(orderOptions);
  }

  public long getOrderOptionPrice() {
    return getOrderOptions().stream()
        .mapToLong(OrderOption::getPrice)
        .sum();
  }

  public List<String> getFormattedOrderOptions() {
    return getOrderOptions().stream()
        .map(option -> "- %s: %s".formatted(name, option.getName()))
        .toList();
  }

}
