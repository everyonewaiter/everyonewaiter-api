package com.everyonewaiter.adapter.web.api.device.request;

import com.everyonewaiter.application.order.request.OrderPaymentWrite;
import com.everyonewaiter.domain.order.entity.OrderPayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPaymentWriteRequest {

  @Schema(name = "OrderPaymentWriteRequest.Approve")
  public record Approve(
      @Schema(description = "결제 수단", example = "CARD", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "결제 수단이 누락되었거나 올바르지 않습니다.")
      OrderPayment.Method method,

      @Schema(description = "결제 금액", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "결제 금액이 누락되었습니다.")
      @Min(value = 1, message = "결제 금액은 1 이상이어야 합니다.")
      long amount,

      @Schema(description = "카드 결제 승인 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 결제 승인 번호가 누락되었습니다.")
      String approvalNo,

      @Schema(description = "카드 할부 개월", example = "00", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 할부 개월이 누락되었습니다.")
      @Size(min = 2, max = 2, message = "카드 할부 개월은 2자리 숫자로 입력해 주세요.")
      String installment,

      @Schema(description = "카드 번호", example = "950002******", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 번호가 누락되었습니다.")
      String cardNo,

      @Schema(description = "카드 발급사명", example = "국민카드", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 발급사명이 누락되었습니다.")
      String issuerName,

      @Schema(description = "카드 매입사명", example = "BC카드", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 매입사명이 누락되었습니다.")
      String purchaseName,

      @Schema(description = "카드사/포인트사 가맹점 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드사/포인트사 가맹점 번호이 누락되었습니다.")
      String merchantNo,

      @Schema(description = "카드 거래일시 YYMMDDHHmmss", example = "250101120000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 거래일시가 누락되었습니다.")
      String tradeTime,

      @Schema(description = "카드 거래 고유 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 거래 고유 번호가 누락되었습니다.")
      String tradeUniqueNo,

      @Schema(description = "부가세", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "부가세가 누락되었습니다.")
      @Min(value = 0, message = "부가세는 0 이상이어야 합니다.")
      long vat,

      @Schema(description = "공급가액", example = "9000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "공급가액이 누락되었습니다.")
      @Min(value = 0, message = "공급가액은 0 이상이어야 합니다.")
      long supplyAmount,

      @Schema(description = "현금 영수증 번호", example = "01044591812", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "현금 영수증 번호가 누락되었습니다.")
      String cashReceiptNo,

      @Schema(description = "현금 영수증 타입", example = "DEDUCTION", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "현금 영수증 타입이 누락되었거나 올바르지 않습니다.")
      OrderPayment.CashReceiptType cashReceiptType
  ) {

    public OrderPaymentWrite.Approve toDomainDto() {
      return new OrderPaymentWrite.Approve(
          method,
          amount,
          approvalNo,
          installment,
          cardNo,
          issuerName,
          purchaseName,
          merchantNo,
          tradeTime,
          tradeUniqueNo,
          vat,
          supplyAmount,
          cashReceiptNo,
          cashReceiptType
      );
    }

  }

  public record Cancel(
      @Schema(description = "카드 결제 승인 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 결제 승인 번호가 누락되었습니다.")
      String approvalNo,

      @Schema(description = "카드 거래일시 YYMMDDHHmmss", example = "250101120000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 거래일시가 누락되었습니다.")
      String tradeTime,

      @Schema(description = "카드 거래 고유 번호", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "카드 거래 고유 번호가 누락되었습니다.")
      String tradeUniqueNo
  ) {

    public OrderPaymentWrite.Cancel toDomainDto() {
      return new OrderPaymentWrite.Cancel(approvalNo, tradeTime, tradeUniqueNo);
    }

  }

}
