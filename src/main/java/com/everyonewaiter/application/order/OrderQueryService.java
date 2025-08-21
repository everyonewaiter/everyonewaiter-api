package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.provided.OrderFinder;
import com.everyonewaiter.application.order.required.OrderRepository;
import com.everyonewaiter.domain.order.OrderView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class OrderQueryService implements OrderFinder {

  private final OrderRepository orderRepository;

  @Override
  @Transactional(readOnly = true)
  public List<OrderView.OrderDetail> findAll(Long storeId, boolean served) {
    return orderRepository.findAll(storeId, served).stream()
        .map(OrderView.OrderDetail::from)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderView.OrderDetail> findAllActive(Long storeId, int tableNo) {
    return orderRepository.findAllActive(storeId, tableNo).stream()
        .map(OrderView.OrderDetail::from)
        .toList();
  }

}
