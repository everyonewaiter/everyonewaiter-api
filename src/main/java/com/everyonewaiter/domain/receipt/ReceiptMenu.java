package com.everyonewaiter.domain.receipt;

import com.everyonewaiter.domain.order.OrderMenu;
import java.util.List;

public record ReceiptMenu(String name, int quantity, List<String> options) {

  public static ReceiptMenu from(OrderMenu orderMenu) {
    return of(orderMenu, orderMenu.getQuantity());
  }

  public static ReceiptMenu of(OrderMenu orderMenu, int updatedQuantity) {
    return new ReceiptMenu(
        orderMenu.getName(),
        updatedQuantity,
        orderMenu.getPrintEnabledOrderOptionGroups()
            .stream()
            .flatMap(orderOptionGroup -> orderOptionGroup.getFormattedOrderOptions().stream())
            .toList()
    );
  }

}
