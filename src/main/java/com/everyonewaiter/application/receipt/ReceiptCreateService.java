package com.everyonewaiter.application.receipt;

import com.everyonewaiter.application.order.provided.OrderFinder;
import com.everyonewaiter.application.receipt.provided.ReceiptCreator;
import com.everyonewaiter.application.receipt.required.ReceiptPrintNoRepository;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class ReceiptCreateService implements ReceiptCreator {

  private final OrderFinder orderFinder;
  private final ReceiptPrintNoRepository receiptRepository;

  @Override
  @Transactional(readOnly = true)
  public Receipt create(Long storeId, int tableNo, Long orderId) {
    Order order = orderFinder.findOrThrow(orderId);

    receiptRepository.increment(storeId);

    return Receipt.of(tableNo, order, receiptRepository.get(storeId));
  }

  @Override
  @Transactional(readOnly = true)
  public Receipt create(Long storeId, int tableNo, List<Long> orderIds) {
    List<Order> orders = orderFinder.findAll(orderIds);

    receiptRepository.increment(storeId);

    return Receipt.of(tableNo, orders, receiptRepository.get(storeId));
  }

  @Override
  @Transactional(readOnly = true)
  public @Nullable Receipt createDiff(
      Long storeId,
      int tableNo,
      List<Order> orders,
      OrderUpdateRequests updateRequests
  ) {
    return Receipt.diff(tableNo, orders, updateRequests, receiptRepository.get(storeId));
  }

  @Override
  @Transactional(readOnly = true)
  public Receipt createCancel(Long storeId, int tableNo, Long orderId) {
    Order order = orderFinder.findOrThrow(orderId);

    receiptRepository.increment(storeId);

    return Receipt.cancel(tableNo, order, receiptRepository.get(storeId));
  }

}
