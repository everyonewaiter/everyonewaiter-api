package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "OrderPaymentDetailResponses")
public record OrderPaymentDetailResponses(List<OrderPaymentView.OrderPaymentDetail> orderPayments) {

  public static OrderPaymentDetailResponses from(List<OrderPayment> orderPayments) {
    return new OrderPaymentDetailResponses(
        orderPayments.stream()
            .map(OrderPaymentView.OrderPaymentDetail::from)
            .toList()
    );
  }

}
