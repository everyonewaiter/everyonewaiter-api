package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.order.OrderOptionGroup.createOrderOptionGroups;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
import com.everyonewaiter.domain.menu.Menu;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "orders_menu")
@Getter
@ToString(exclude = {"order", "orderOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderMenu extends AggregateEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "orders_id",
      foreignKey = @ForeignKey(name = "fk_orders_menu_orders_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      nullable = false,
      updatable = false
  )
  private Order order;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Column(name = "image", nullable = false, length = 30)
  private String image;

  @Embedded
  private Serving serving;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

  public static OrderMenu create(
      Map<Long, Menu> menus,
      Order order,
      OrderMenuModifyRequest request
  ) {
    validateOrderMenuCreate(menus, request);

    Menu menu = menus.get(request.menuId());

    OrderMenu orderMenu = new OrderMenu();

    orderMenu.order = requireNonNull(order);
    orderMenu.name = requireNonNull(menu.getName());
    orderMenu.price = menu.getPrice();
    orderMenu.quantity = requireNonNull(request.quantity());
    orderMenu.image = menu.getImage();
    orderMenu.serving = new Serving();
    orderMenu.printEnabled = menu.isPrintEnabled();
    orderMenu.orderOptionGroups.addAll(
        createOrderOptionGroups(menu, orderMenu, request.menuOptionGroups())
    );

    return orderMenu;
  }

  public static List<OrderMenu> createOrderMenus(
      Map<Long, Menu> menus,
      Order order,
      List<OrderMenuModifyRequest> requests
  ) {
    return requests.stream()
        .map(request -> create(menus, order, request))
        .toList();
  }

  private static void validateOrderMenuCreate(
      Map<Long, Menu> menus,
      OrderMenuModifyRequest request
  ) {
    if (!menus.containsKey(request.menuId())) {
      throw new OrderMenuNotFoundException();
    }

    if (!menus.get(request.menuId()).canOrder()) {
      throw new IncludeSoldOutMenuException();
    }

    if (request.quantity() <= 0) {
      throw new InvalidOrderMenuQuantityException();
    }
  }

  public void serving() {
    this.serving.complete();
  }

  public void toggleServing() {
    if (this.serving.isServed()) {
      this.serving.cancel();
    } else {
      this.serving.complete();
    }
  }

  public void update(int quantity) {
    if (quantity <= 0) {
      throw new InvalidOrderMenuQuantityException();
    }

    this.quantity = quantity;
  }

  public long calculateTotalPrice() {
    long totalPrice = price;

    for (OrderOptionGroup orderOptionGroup : getOrderOptionGroups()) {
      totalPrice += orderOptionGroup.calculateTotalPrice();
    }

    return totalPrice * quantity;
  }

  public List<OrderOptionGroup> getPrintEnabledOrderOptionGroups() {
    return getOrderOptionGroups().stream()
        .filter(OrderOptionGroup::isPrintEnabled)
        .toList();
  }

  public List<OrderOptionGroup> getOrderOptionGroups() {
    return Collections.unmodifiableList(orderOptionGroups);
  }

}
