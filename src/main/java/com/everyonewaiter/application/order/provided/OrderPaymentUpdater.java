package com.everyonewaiter.application.order.provided;

import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentCashReceiptIssueRequest;
import jakarta.validation.Valid;

public interface OrderPaymentUpdater {

  OrderPayment issueCashReceipt(
      Long storeId,
      int tableNo,
      Long orderPaymentId,
      @Valid OrderPaymentCashReceiptIssueRequest issueRequest
  );

}
