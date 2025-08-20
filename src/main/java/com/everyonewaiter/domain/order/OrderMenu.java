package com.everyonewaiter.domain.order;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString(exclude = {"order", "orderOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderMenu extends AggregateEntity {

  private Order order;

  private String name;

  private long price;

  private int quantity;

  private Serving serving = new Serving();

  private boolean printEnabled;

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
