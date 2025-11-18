package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.order.OrderOption.createOrderOptions;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuOptionGroup;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "orders_option_group")
@Getter
@ToString(exclude = {"orderMenu", "orderOptions"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderOptionGroup extends AggregateEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "orders_menu_id",
      foreignKey = @ForeignKey(name = "fk_orders_option_group_orders_menu_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      nullable = false,
      updatable = false
  )
  private OrderMenu orderMenu;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @ElementCollection
  @CollectionTable(
      name = "orders_option",
      foreignKey = @ForeignKey(name = "fk_orders_option_orders_option_group_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      joinColumns = @JoinColumn(name = "orders_option_group_id", nullable = false)
  )
  @OrderBy("position asc")
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

  public List<String> getFormattedOrderOptions() {
    return getOrderOptions().stream()
        .map(option -> "- %s: %s".formatted(name, option.getName()))
        .toList();
  }

  public List<OrderOption> getOrderOptions() {
    return Collections.unmodifiableList(orderOptions);
  }

}
