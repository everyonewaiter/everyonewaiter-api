package com.everyonewaiter.presentation.device.request;

import com.everyonewaiter.application.order.request.OrderWrite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PosWriteRequest {

  @Schema(name = "PosWriteRequest.Discount")
  public record Discount(
      @Schema(description = "할인 금액", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "할인 금액이 누락되었습니다.")
      @Min(value = 0, message = "할인 금액은 0 이상으로 입력해 주세요.")
      long discountPrice
  ) {

  }

  @Schema(name = "PosWriteRequest.UpdateOrders")
  public record UpdateOrders(
      @Schema(description = "주문 목록", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 목록이 누락되었습니다.")
      @Size(min = 1, message = "주문 목록을 1개 이상 입력해 주세요.")
      List<@Valid UpdateOrder> orders
  ) {

    public OrderWrite.UpdateOrders toDomainDto() {
      return new OrderWrite.UpdateOrders(
          orders.stream()
              .map(UpdateOrder::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "PosWriteRequest.UpdateOrder")
  public record UpdateOrder(
      @Schema(description = "주문 ID", example = "694865267482835533", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 ID가 누락되었습니다.")
      Long orderId,

      @Schema(description = "주문 메뉴 목록", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 목록이 누락되었습니다.")
      @Size(min = 1, message = "주문 메뉴 목록을 1개 이상 입력해 주세요.")
      List<@Valid UpdateOrderMenu> orderMenus
  ) {

    public OrderWrite.UpdateOrder toDomainDto() {
      return new OrderWrite.UpdateOrder(
          orderId,
          orderMenus.stream()
              .map(UpdateOrderMenu::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "PosWriteRequest.UpdateOrder")
  public record UpdateOrderMenu(
      @Schema(description = "주문 메뉴 ID", example = "694865267482835533", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 ID가 누락되었습니다.")
      Long orderMenuId,

      @Schema(description = "주문 메뉴 수량", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 수량이 누락되었습니다.")
      @Min(value = 0, message = "주문 메뉴 수량은 0 이상으로 입력해 주세요.")
      int quantity
  ) {

    public OrderWrite.UpdateOrderMenu toDomainDto() {
      return new OrderWrite.UpdateOrderMenu(orderMenuId, quantity);
    }

  }

  @Schema(name = "PosWriteRequest.UpdateMemo")
  public record UpdateMemo(
      @Schema(description = "주문 메모", example = "13시 포장", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메모 정보가 누락되었습니다.")
      @Size(max = 30, message = "주문 메모는 30자 이하로 입력해 주세요.")
      String memo
  ) {

  }

}
