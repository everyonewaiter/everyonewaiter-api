package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.entity.ReceiptMenu;
import com.everyonewaiter.domain.order.repository.ReceiptRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptFactory {

  private final ReceiptRepository receiptRepository;

  public Receipt createReceipt(Order order) {
    receiptRepository.incrementPrintNo(order.getStore().getId());
    return createReceipt(
        order.getStore().getId(),
        order.getMemo(),
        order.getPrintEnabledOrderMenus()
    );
  }

  public Receipt createReceipt(Long storeId, List<OrderMenu> orderMenus) {
    return createReceipt(storeId, "", orderMenus);
  }

  private Receipt createReceipt(Long storeId, String memo, List<OrderMenu> orderMenus) {
    Receipt receipt = new Receipt(
        memo,
        receiptRepository.getPrintNo(storeId),
        orderMenus.stream()
            .map(this::createReceiptMenu)
            .toList()
    );
    receiptRepository.incrementPrintNo(storeId);
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
