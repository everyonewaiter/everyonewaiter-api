package com.everyonewaiter.application.order;

import com.everyonewaiter.application.order.request.OrderPaymentWrite;
import com.everyonewaiter.application.order.response.OrderPaymentResponse;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.order.repository.OrderPaymentRepository;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

  private final PosTableActivityRepository posTableActivityRepository;
  private final OrderPaymentRepository orderPaymentRepository;

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public Long approve(Long storeId, int tableNo, OrderPaymentWrite.Approve request) {
    PosTableActivity posTableActivity =
        posTableActivityRepository.findActiveOrThrow(storeId, tableNo);

    OrderPayment orderPayment = OrderPayment.approve(
        posTableActivity,
        request.method(),
        request.amount(),
        request.approvalNo(),
        request.installment(),
        request.cardNo(),
        request.issuerName(),
        request.purchaseName(),
        request.merchantNo(),
        request.tradeTime(),
        request.tradeUniqueNo(),
        request.vat(),
        request.supplyAmount(),
        request.cashReceiptNo(),
        request.cashReceiptType()
    );

    return orderPaymentRepository.save(orderPayment).getId();
  }

  @Transactional
  public Long cancel(
      Long storeId,
      Long orderPaymentId,
      OrderPaymentWrite.Cancel request
  ) {
    OrderPayment approveOrderPayment =
        orderPaymentRepository.findByIdAndStoreId(orderPaymentId, storeId);

    OrderPayment cancelOrderPayment = OrderPayment.cancel(
        approveOrderPayment,
        request.approvalNo(),
        request.tradeTime(),
        request.tradeUniqueNo()
    );

    return orderPaymentRepository.save(cancelOrderPayment).getId();
  }

  public OrderPaymentResponse.Details readAllByPos(Long storeId, Instant start, Instant end) {
    List<OrderPayment> payments =
        orderPaymentRepository.findAllByStoreIdAndDate(storeId, start, end);
    return OrderPaymentResponse.Details.from(payments);
  }

}
