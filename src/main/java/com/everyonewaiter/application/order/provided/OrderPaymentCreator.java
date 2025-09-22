package com.everyonewaiter.application.order.provided;

import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentApproveRequest;
import com.everyonewaiter.domain.order.OrderPaymentCancelRequest;
import jakarta.validation.Valid;

public interface OrderPaymentCreator {

  OrderPayment approve(
      Long storeId,
      int tableNo,
      @Valid OrderPaymentApproveRequest approveRequest
  );

  OrderPayment cancel(
      Long storeId,
      Long orderPaymentId,
      @Valid OrderPaymentCancelRequest cancelRequest
  );

}
