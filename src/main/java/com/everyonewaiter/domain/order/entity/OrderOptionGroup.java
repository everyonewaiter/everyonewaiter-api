package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "orders_option_group")
@Entity
@Getter
@ToString(exclude = {"orderMenu", "orderOptions"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOptionGroup extends Aggregate {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "orders_menu_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private OrderMenu orderMenu;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @ElementCollection
  @CollectionTable(name = "orders_option", joinColumns = @JoinColumn(name = "orders_option_group_id"))
  @OrderBy("position asc")
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

  public long getOrderOptionPrice() {
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
