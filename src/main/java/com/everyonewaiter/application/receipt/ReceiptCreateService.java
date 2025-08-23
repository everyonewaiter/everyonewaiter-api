package com.everyonewaiter.application.receipt;

import com.everyonewaiter.application.order.provided.OrderFinder;
import com.everyonewaiter.application.receipt.provided.ReceiptCreator;
import com.everyonewaiter.application.receipt.required.ReceiptPrintNoRepository;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ReceiptCreateService implements ReceiptCreator {

  private final OrderFinder orderFinder;
  private final ReceiptPrintNoRepository receiptRepository;

  @Override
  @Transactional(readOnly = true)
  public Receipt create(Long storeId, Long orderId) {
    Order order = orderFinder.findOrThrow(orderId);

    receiptRepository.increment(storeId);

    return Receipt.of(order, receiptRepository.get(storeId));
  }

  @Override
  @Transactional(readOnly = true)
  public Receipt create(Long storeId, List<Long> orderIds) {
    List<Order> orders = orderFinder.findAll(orderIds);

    receiptRepository.increment(storeId);

    return Receipt.of(orders, receiptRepository.get(storeId));
  }

  @Override
  @Transactional(readOnly = true)
  public Receipt createDiff(Long storeId, List<Order> orders, OrderUpdateRequests updateRequests) {
    return Receipt.diff(orders, updateRequests, receiptRepository.get(storeId));
  }

}
