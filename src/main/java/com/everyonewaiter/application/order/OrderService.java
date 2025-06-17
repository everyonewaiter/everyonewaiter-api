package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.request.OrderWrite;
import com.everyonewaiter.application.order.response.OrderResponse;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.OrderOptionGroup;
import com.everyonewaiter.domain.order.repository.OrderRepository;
import com.everyonewaiter.domain.order.service.OrderFactory;
import com.everyonewaiter.domain.order.service.OrderValidator;
import com.everyonewaiter.global.annotation.RedissonLock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final MenuRepository menuRepository;
  private final OrderValidator orderValidator;
  private final OrderFactory orderFactory;
  private final OrderRepository orderRepository;

  @Transactional
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public Long createOrder(Long storeId, int tableNo, OrderWrite.Create request) {
    Set<Long> menuIds = request.orderMenus()
        .stream().map(OrderWrite.OrderMenu::menuId)
        .collect(Collectors.toSet());
    orderValidator.validateCreateOrder(storeId, menuIds);

    Map<Long, Menu> menus = findMenus(storeId, menuIds);
    Order order = orderFactory.createOrder(storeId, tableNo, request.type(), request.memo());

    for (OrderWrite.OrderMenu orderMenuCreateRequest : request.orderMenus()) {
      Menu menu = menus.get(orderMenuCreateRequest.menuId());
      int quantity = orderMenuCreateRequest.quantity();

      OrderMenu orderMenu = orderFactory.createOrderMenu(order, menu, quantity);
      for (OrderWrite.OptionGroup orderMenuOptionGroup : orderMenuCreateRequest.menuOptionGroups()) {
        Long menuOptionGroupId = orderMenuOptionGroup.menuOptionGroupId();

        Map<String, Long> orderOptions = orderMenuOptionGroup.orderOptions()
            .stream()
            .collect(Collectors.toMap(OrderWrite.Option::name, OrderWrite.Option::price));
        OrderOptionGroup orderOptionGroup =
            orderFactory.createOrderOptionGroup(orderMenu, menu, menuOptionGroupId, orderOptions);

        orderMenu.addOrderOptionGroup(orderOptionGroup);
      }

      order.addOrderMenu(orderMenu);
    }

    return orderRepository.save(order).getId();
  }

  private Map<Long, Menu> findMenus(Long storeId, Set<Long> menuIds) {
    return menuRepository.findAllByStoreIdAndIds(storeId, menuIds.stream().toList())
        .stream()
        .collect(Collectors.toMap(Menu::getId, menu -> menu));
  }

  @Transactional
  public void servingOrder(Long storeId, Long orderId) {
    Order order = orderRepository.findByIdAndStoreIdOrThrow(orderId, storeId);
    order.serving();
    orderRepository.save(order);
  }

  @Transactional
  public void servingOrderMenu(Long storeId, Long orderId, Long orderMenuId) {
    Order order = orderRepository.findByIdAndStoreIdOrThrow(orderId, storeId);
    order.servingMenu(orderMenuId);
    orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public OrderResponse.Details readAllByHall(Long storeId, boolean served) {
    List<Order> orders = orderRepository.findAllByStoreIdAndServed(storeId, served);
    return OrderResponse.Details.from(orders);
  }

}
