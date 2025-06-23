package com.everyonewaiter.application.order.request;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPaymentWrite {

  public record Approve(
      OrderPayment.Method method,
      long amount,
      String approvalNo,
      String installment,
      String cardNo,
      String issuerName,
      String purchaseName,
      String merchantNo,
      String tradeTime,
      String tradeUniqueNo,
      long vat,
      long supplyAmount,
      String cashReceiptNo,
      OrderPayment.CashReceiptType cashReceiptType
  ) {

  }

  public record Cancel(String approvalNo, String tradeTime, String tradeUniqueNo) {

  }

}
