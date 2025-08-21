package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.order.OrderView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "OrderDetailResponses")
public record OrderDetailResponses(List<OrderView.OrderDetail> orders) {

  public static OrderDetailResponses from(List<OrderView.OrderDetail> orders) {
    return new OrderDetailResponses(orders);
  }

}
