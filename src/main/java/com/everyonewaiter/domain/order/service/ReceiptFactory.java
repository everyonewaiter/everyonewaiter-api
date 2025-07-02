package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.entity.ReceiptMenu;
import com.everyonewaiter.domain.order.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptFactory {

  private final ReceiptRepository receiptRepository;

  public Receipt createReceipt(Order order) {
    Receipt receipt = new Receipt(
        order.getMemo(),
        receiptRepository.getPrintNo(order.getStore().getId()),
        order.getPrintEnabledOrderMenus()
            .stream()
            .map(this::createReceiptMenu)
            .toList()
    );
    receiptRepository.incrementPrintNo(order.getStore().getId());
    return receipt;
  }

  public ReceiptMenu createReceiptMenu(OrderMenu orderMenu) {
    return new ReceiptMenu(
        orderMenu.getName(),
        orderMenu.getQuantity(),
        orderMenu.getPrintEnabledOrderOptionGroups()
            .stream()
            .flatMap(orderOptionGroup -> orderOptionGroup.getFormattedOrderOptions().stream())
            .toList()
    );
  }

}
