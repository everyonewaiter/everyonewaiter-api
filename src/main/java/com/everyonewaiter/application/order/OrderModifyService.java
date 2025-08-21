package com.everyonewaiter.application.order;

import com.everyonewaiter.application.menu.provided.MenuFinder;
import com.everyonewaiter.application.order.provided.OrderServer;
import com.everyonewaiter.application.order.required.OrderRepository;
import com.everyonewaiter.application.pos.provided.PosTableActivityFinder;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.order.EmptyShoppingCartException;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderCreateRequest;
import com.everyonewaiter.domain.order.OrderMenuModifyRequest;
import com.everyonewaiter.domain.order.OrderMenuNotFoundException;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.pos.PosTableActivity;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class OrderModifyService implements OrderServer {

  private final MenuFinder menuFinder;
  private final PosTableActivityFinder activityFinder;
  private final OrderRepository orderRepository;

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public Order create(Long storeId, OrderType orderType, OrderCreateRequest createRequest) {
    Map<Long, Menu> menus = findMenus(storeId, createRequest);
    PosTableActivity activity = activityFinder.findActiveOrCreate(storeId, createRequest.tableNo());

    Order order = Order.create(menus, activity, orderType, createRequest);

    return orderRepository.save(order);
  }

  private Map<Long, Menu> findMenus(Long storeId, OrderCreateRequest createRequest) {
    Set<Long> menuIds = createRequest.orderMenus()
        .stream()
        .map(OrderMenuModifyRequest::menuId)
        .collect(Collectors.toSet());

    if (menuIds.isEmpty()) {
      throw new EmptyShoppingCartException();
    }

    Map<Long, Menu> menus = menuFinder.findAll(storeId, menuIds.stream().toList())
        .stream()
        .collect(Collectors.toMap(Menu::getId, menu -> menu));

    if (menus.size() != menuIds.size()) {
      throw new OrderMenuNotFoundException();
    }

    return menus;
  }

  @Override
  public Order serving(Long storeId, Long orderId) {
    Order order = orderRepository.findOrThrow(orderId, storeId);

    order.serving();

    return orderRepository.save(order);
  }

  @Override
  public Order serving(Long storeId, Long orderId, Long orderMenuId) {
    Order order = orderRepository.findOrThrow(orderId, storeId);

    order.serving(orderMenuId);

    return orderRepository.save(order);
  }

}
