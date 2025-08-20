package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuOptionGroup;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderMenu;
import com.everyonewaiter.domain.order.OrderOption;
import com.everyonewaiter.domain.order.OrderOptionGroup;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.shared.Position;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFactory {

  private final PosTableRepository posTableRepository;
  private final PosTableActivityRepository posTableActivityRepository;

  public Order createOrder(Long storeId, int tableNo, OrderType type, String memo) {
    PosTableActivity posTableActivity = getOrCreatePosTableActivity(storeId, tableNo);
    return Order.create(posTableActivity, type, memo);
  }

  public OrderMenu createOrderMenu(Order order, Menu menu, int quantity) {
    return OrderMenu.create(
        order,
        menu.getName(),
        menu.getPrice(),
        quantity,
        menu.isPrintEnabled()
    );
  }

  public OrderOptionGroup createOrderOptionGroup(
      OrderMenu orderMenu,
      Menu menu,
      Long menuOptionGroupId,
      Map<String, Long> orderOptions
  ) {
    MenuOptionGroup menuOptionGroup = menu.getMenuOptionGroup(menuOptionGroupId);

    OrderOptionGroup orderOptionGroup = OrderOptionGroup.create(
        orderMenu,
        menuOptionGroup.getName(),
        menuOptionGroup.isPrintEnabled()
    );
    orderOptions.forEach((name, price) -> {
      Position position = menuOptionGroup.getMenuOption(name, price).getPosition();
      OrderOption orderOption = OrderOption.create(name, price, position);
      orderOptionGroup.addOrderOption(orderOption);
    });

    return orderOptionGroup;
  }

  private PosTableActivity getOrCreatePosTableActivity(Long storeId, int tableNo) {
    Optional<PosTableActivity> posTableActivity =
        posTableActivityRepository.findActive(storeId, tableNo);

    if (posTableActivity.isPresent()) {
      return posTableActivity.get();
    } else {
      PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
      PosTableActivity newActivity = PosTableActivity.create(posTable);
      return posTableActivityRepository.save(newActivity);
    }
  }

}
