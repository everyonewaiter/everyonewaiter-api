package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.order.OrderView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "OrderHallResponses")
public record OrderHallResponses(
    List<OrderView.OrderDetail> served,
    List<OrderView.OrderDetail> unserved
) {

  public static OrderHallResponses of(
      List<OrderView.OrderDetail> served,
      List<OrderView.OrderDetail> unserved
  ) {
    return new OrderHallResponses(served, unserved);
  }

}
