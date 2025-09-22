package com.everyonewaiter.domain.receipt;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderMenu;
import com.everyonewaiter.domain.order.OrderMenuNotFoundException;
import com.everyonewaiter.domain.order.OrderMenuQuantityUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Receipt(int tableNo, String memo, int printNo, List<ReceiptMenu> receiptMenus) {

  public static Receipt of(int tableNo, Order order, int printNo) {
    return new Receipt(
        tableNo,
        order.getMemo(),
        printNo,
        order.getPrintEnabledOrderMenus()
            .stream()
            .map(ReceiptMenu::from)
            .toList()
    );
  }

  public static Receipt of(int tableNo, List<Order> orders, int printNo) {
    return new Receipt(
        tableNo,
        "",
        printNo,
        orders.stream()
            .flatMap(order -> order.getPrintEnabledOrderMenus().stream())
            .map(ReceiptMenu::from)
            .toList()
    );
  }

  public static Receipt diff(
      int tableNo,
      List<Order> orders,
      OrderUpdateRequests updateRequests,
      int printNo
  ) {
    Map<Long, OrderMenu> beforeOrderMenus = orders.stream()
        .flatMap(order -> order.getPrintEnabledOrderMenus().stream())
        .collect(Collectors.toMap(OrderMenu::getId, orderMenu -> orderMenu));

    List<OrderMenuQuantityUpdateRequest> afterOrderMenus = updateRequests.orders()
        .stream()
        .flatMap(order -> order.orderMenus().stream())
        .toList();

    List<ReceiptMenu> receiptMenus = new ArrayList<>();
    for (OrderMenuQuantityUpdateRequest afterOrderMenu : afterOrderMenus) {
      if (!beforeOrderMenus.containsKey(afterOrderMenu.orderMenuId())) {
        throw new OrderMenuNotFoundException();
      }

      OrderMenu orderMenu = beforeOrderMenus.get(afterOrderMenu.orderMenuId());

      int updatedQuantity = afterOrderMenu.quantity() - orderMenu.getQuantity();

      if (updatedQuantity != 0) {
        ReceiptMenu receiptMenu = ReceiptMenu.of(orderMenu, updatedQuantity);

        receiptMenus.add(receiptMenu);
      }
    }

    return new Receipt(tableNo, "", printNo, receiptMenus);
  }

  public static Receipt cancel(int tableNo, Order order, int printNo) {
    return new Receipt(
        tableNo,
        "",
        printNo,
        order.getPrintEnabledOrderMenus()
            .stream()
            .map(ReceiptMenu::cancel)
            .toList()
    );
  }

}
