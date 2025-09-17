package com.everyonewaiter.domain.receipt;

import com.everyonewaiter.domain.order.OrderMenu;
import java.util.List;

public record ReceiptMenu(String name, int quantity, List<String> options) {

  public static ReceiptMenu from(OrderMenu orderMenu) {
    return of(orderMenu, orderMenu.getQuantity());
  }

  public static ReceiptMenu of(OrderMenu orderMenu, int quantity) {
    return new ReceiptMenu(
        orderMenu.getName(),
        quantity,
        orderMenu.getPrintEnabledOrderOptionGroups()
            .stream()
            .flatMap(orderOptionGroup -> orderOptionGroup.getFormattedOrderOptions().stream())
            .toList()
    );
  }

  public static ReceiptMenu cancel(OrderMenu orderMenu) {
    return of(orderMenu, orderMenu.getQuantity() * -1);
  }

}
