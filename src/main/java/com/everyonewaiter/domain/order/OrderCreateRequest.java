package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "OrderCreateRequest")
public record OrderCreateRequest(
    @Schema(description = "테이블 번호", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "테이블 번호가 누락되었습니다.")
    @Min(value = 1, message = "테이블 번호는 1 이상으로 입력해 주세요.")
    @Max(value = 10_100, message = "테이블 번호는 10,100 이하로 입력해 주세요.")
    Integer tableNo,

    @Schema(description = "주문 메모", example = "13시 포장", requiredMode = REQUIRED)
    @NotNull(message = "주문 메모 정보가 누락되었습니다.")
    @Size(max = 10, message = "주문 메모는 10자 이하로 입력해 주세요.")
    String memo,

    @Schema(description = "주문 메뉴", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 정보가 누락되었습니다.")
    @Size(min = 1, message = "장바구니에 메뉴를 1개 이상 담아 주세요.")
    List<@Valid OrderMenuModifyRequest> orderMenus
) {

}
