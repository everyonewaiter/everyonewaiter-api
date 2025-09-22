package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "OrderPaymentCancelRequest")
public record OrderPaymentCancelRequest(
    @Schema(description = "카드 결제 승인 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "카드 결제 승인 번호가 누락되었습니다.")
    String approvalNo,

    @Schema(description = "카드 거래일시 YYMMDDHHmmss", example = "250101120000", requiredMode = REQUIRED)
    @NotNull(message = "카드 거래일시가 누락되었습니다.")
    String tradeTime,

    @Schema(description = "카드 거래 고유 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "카드 거래 고유 번호가 누락되었습니다.")
    String tradeUniqueNo
) {

}
