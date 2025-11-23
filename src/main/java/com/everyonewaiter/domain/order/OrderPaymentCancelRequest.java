package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.everyonewaiter.domain.support.DateFormatter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "OrderPaymentCancelRequest")
public record OrderPaymentCancelRequest(
    @Schema(description = "카드 결제 승인 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "카드 결제 승인 번호가 누락되었습니다.")
    String approvalNo,

    @Schema(description = "카드 거래일시 yyMMdd", example = "250101", requiredMode = REQUIRED)
    @NotNull(message = "카드 거래일시가 누락되었습니다.")
    @Size(max = 6, message = "카드 거래일시는 최대 6자 이하로 입력해 주세요.")
    String tradeTime,

    @Schema(description = "카드 거래 고유 번호", example = "1234567890", requiredMode = REQUIRED)
    @NotNull(message = "카드 거래 고유 번호가 누락되었습니다.")
    String tradeUniqueNo
) {

  public String approvalNo(boolean isPureCash) {
    return isPureCash ? "" : approvalNo;
  }

  public String tradeTime(boolean isPureCash) {
    return isPureCash ? DateFormatter.getSimpleKstDate() : tradeTime;
  }

  public String tradeUniqueNo(boolean isPureCash) {
    return isPureCash ? "" : tradeUniqueNo;
  }

}
