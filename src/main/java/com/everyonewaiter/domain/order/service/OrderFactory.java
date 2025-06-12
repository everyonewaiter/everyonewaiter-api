package com.everyonewaiter.domain.order.service;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.OrderOption;
import com.everyonewaiter.domain.order.entity.OrderOptionGroup;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.global.domain.entity.Position;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFactory {

  private final PosTableRepository posTableRepository;
  private final PosTableActivityRepository posTableActivityRepository;

  public Order createOrder(Long storeId, int tableNo, Order.Type type, String memo) {
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
      OrderOption orderOption = OrderOption.create(name, price, position.getValue());
      orderOptionGroup.addOrderOption(orderOption);
    });

    return orderOptionGroup;
  }

  private PosTableActivity getOrCreatePosTableActivity(Long storeId, int tableNo) {
    Optional<PosTableActivity> posTableActivity =
        posTableActivityRepository.findByStoreIdAndTableNo(storeId, tableNo);

    if (posTableActivity.isPresent()) {
      return posTableActivity.get();
    } else {
      PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
      PosTableActivity newActivity = PosTableActivity.create(posTable);
      return posTableActivityRepository.save(newActivity);
    }
  }

}
