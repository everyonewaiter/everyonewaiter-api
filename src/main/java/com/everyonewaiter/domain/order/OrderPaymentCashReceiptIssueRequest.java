package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "OrderPaymentCashReceiptIssueRequest")
public record OrderPaymentCashReceiptIssueRequest(
    @Schema(description = "KSCAT 결제 승인 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "KSCAT 결제 승인 번호가 누락되었습니다.")
    String approvalNo,

    @Schema(description = "KSCAT 거래일시 yyMMdd", example = "250101", requiredMode = REQUIRED)
    @NotNull(message = "KSCAT 거래일시가 누락되었습니다.")
    @Size(max = 6, message = "KSCAT 거래일시는 최대 6자 이하로 입력해 주세요.")
    String tradeTime,

    @Schema(description = "KSCAT 거래 고유 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "KSCAT 거래 고유 번호가 누락되었습니다.")
    String tradeUniqueNo,

    @Schema(description = "현금 영수증 번호", example = "01044591812", requiredMode = REQUIRED)
    @NotNull(message = "현금 영수증 번호가 누락되었습니다.")
    String cashReceiptNo,

    @Schema(description = "현금 영수증 타입", example = "DEDUCTION", requiredMode = REQUIRED)
    @NotNull(message = "현금 영수증 타입이 누락되었거나 올바르지 않습니다.")
    CashReceiptType cashReceiptType
) {

}
