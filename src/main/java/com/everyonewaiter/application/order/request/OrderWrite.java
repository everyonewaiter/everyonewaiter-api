package com.everyonewaiter.application.order.request;

import com.everyonewaiter.domain.order.entity.Order;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderWrite {

  public record Create(Order.Type type, String memo, List<OrderMenu> orderMenus) {

  }

  public record OrderMenu(Long menuId, int quantity, List<OptionGroup> menuOptionGroups) {

  }

  public record OptionGroup(Long menuOptionGroupId, List<Option> orderOptions) {

  }

  public record Option(String name, long price) {

  }

  public record UpdateOrders(List<UpdateOrder> orders) {

  }

  public record UpdateOrder(Long orderId, List<UpdateOrderMenu> orderMenus) {

  }

  public record UpdateOrderMenu(Long orderMenuId, int quantity) {

  }

}
