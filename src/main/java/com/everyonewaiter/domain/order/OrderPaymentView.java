package com.everyonewaiter.domain.order;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class OrderPaymentView {

  @Schema(name = "OrderPaymentView.OrderPaymentDetail")
  public record OrderPaymentDetail(
      @Schema(description = "주문 결제 ID", example = "\"694865267482835533\"")
      String orderPaymentId,

      @Schema(description = "POS 테이블 액티비티 ID", example = "\"694865267482835533\"")
      String posTableActivityId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "결제 상태", example = "APPROVE")
      OrderPaymentState state,

      @Schema(description = "결제 수단", example = "CARD")
      OrderPaymentMethod method,

      @Schema(description = "결제 금액", example = "10000")
      long amount,

      @Schema(description = "결제 취소 가능 여부", example = "true")
      boolean cancellable,

      @Schema(description = "카드 결제 승인 번호", example = "1234567890")
      String approvalNo,

      @Schema(description = "카드 할부 개월", example = "00")
      String installment,

      @Schema(description = "카드 번호", example = "950002******")
      String cardNo,

      @Schema(description = "카드 발급사명", example = "국민카드")
      String issuerName,

      @Schema(description = "카드 매입사명", example = "BC카드")
      String purchaseName,

      @Schema(description = "카드사/포인트사 가맹점 번호", example = "1234567890")
      String merchantNo,

      @Schema(description = "카드 거래일시 YYMMDDHHmmss", example = "250101120000")
      String tradeTime,

      @Schema(description = "카드 거래 고유 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 거래 고유 번호가 누락되었습니다.")
      String tradeUniqueNo,

      @Schema(description = "부가세", example = "1000")
      long vat,

      @Schema(description = "공급가액", example = "9000")
      long supplyAmount,

      @Schema(description = "현금 영수증 번호", example = "01044591812")
      String cashReceiptNo,

      @Schema(description = "현금 영수증 타입", example = "DEDUCTION")
      CashReceiptType cashReceiptType,

      @Schema(description = "주문 결제 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt
  ) {

    public static OrderPaymentDetail from(OrderPayment orderPayment) {
      return new OrderPaymentDetail(
          String.valueOf(orderPayment.getId()),
          String.valueOf(orderPayment.getPosTableActivity().getId()),
          String.valueOf(orderPayment.getStore().getId()),
          orderPayment.getState(),
          orderPayment.getMethod(),
          orderPayment.getAmount(),
          orderPayment.isCancellable(),
          orderPayment.getApprovalNo(),
          orderPayment.getInstallment(),
          orderPayment.getCardNo(),
          orderPayment.getIssuerName(),
          orderPayment.getPurchaseName(),
          orderPayment.getMerchantNo(),
          orderPayment.getTradeTime(),
          orderPayment.getTradeUniqueNo(),
          orderPayment.getVat(),
          orderPayment.getSupplyAmount(),
          orderPayment.getCashReceiptNo(),
          orderPayment.getCashReceiptType(),
          orderPayment.getCreatedAt()
      );
    }

  }

}
