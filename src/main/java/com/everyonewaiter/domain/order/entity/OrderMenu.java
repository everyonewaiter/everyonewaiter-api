package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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


@Table(name = "orders_menu")
@Entity
@Getter
@ToString(exclude = {"order", "orderOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu extends Aggregate {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "orders_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Order order;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Embedded
  private Serving serving = new Serving();

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

  public static OrderMenu create(
      Order order,
      String name,
      long price,
      int quantity,
      boolean printEnabled
  ) {
    if (quantity > 0) {
      OrderMenu orderMenu = new OrderMenu();
      orderMenu.order = order;
      orderMenu.name = name;
      orderMenu.price = price;
      orderMenu.quantity = quantity;
      orderMenu.printEnabled = printEnabled;
      return orderMenu;
    } else {
      throw new BusinessException(ErrorCode.ORDER_MENU_QUANTITY_POSITIVE);
    }
  }

  public void addOrderOptionGroup(OrderOptionGroup orderOptionGroup) {
    orderOptionGroups.add(orderOptionGroup);
  }

  public void serving() {
    if (!this.serving.isServed()) {
      this.serving.complete();
    }
  }

  public void servingOrCancel() {
    if (this.serving.isServed()) {
      this.serving.cancel();
    } else {
      this.serving.complete();
    }
  }

  public void updateQuantity(int quantity) {
    if (quantity > 0) {
      this.quantity = quantity;
    } else {
      throw new BusinessException(ErrorCode.ORDER_MENU_QUANTITY_POSITIVE);
    }
  }

  public long calculateTotalPrice() {
    long totalPrice = price;
    for (OrderOptionGroup orderOptionGroup : orderOptionGroups) {
      totalPrice += orderOptionGroup.getOrderOptionPrice();
    }
    return totalPrice * quantity;
  }

  public List<OrderOptionGroup> getOrderOptionGroups() {
    return Collections.unmodifiableList(orderOptionGroups);
  }

  public List<OrderOptionGroup> getPrintEnabledOrderOptionGroups() {
    return getOrderOptionGroups().stream()
        .filter(OrderOptionGroup::isPrintEnabled)
        .toList();
  }

}
