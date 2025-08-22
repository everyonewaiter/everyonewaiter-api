package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.provided.OrderPaymentCreator;
import com.everyonewaiter.application.order.provided.OrderPaymentFinder;
import com.everyonewaiter.application.order.required.OrderPaymentRepository;
import com.everyonewaiter.application.pos.provided.PosTableActivityFinder;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentApproveRequest;
import com.everyonewaiter.domain.order.OrderPaymentCancelRequest;
import com.everyonewaiter.domain.pos.PosTableActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class OrderPaymentModifyService implements OrderPaymentCreator {

  private final PosTableActivityFinder activityFinder;
  private final OrderPaymentFinder orderPaymentFinder;
  private final OrderPaymentRepository orderPaymentRepository;

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public OrderPayment approve(
      Long storeId,
      int tableNo,
      OrderPaymentApproveRequest approveRequest
  ) {
    PosTableActivity posTableActivity = activityFinder.findActiveOrThrow(storeId, tableNo);

    OrderPayment orderPayment = OrderPayment.approve(posTableActivity, approveRequest);

    return orderPaymentRepository.save(orderPayment);
  }

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public OrderPayment cancel(
      Long storeId,
      Long orderPaymentId,
      OrderPaymentCancelRequest cancelRequest
  ) {
    OrderPayment approveOrderPayment = orderPaymentFinder.findOrThrow(orderPaymentId, storeId);

    OrderPayment cancelOrderPayment = OrderPayment.cancel(approveOrderPayment, cancelRequest);

    return orderPaymentRepository.save(cancelOrderPayment);
  }

}
