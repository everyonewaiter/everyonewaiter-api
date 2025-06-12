package com.everyonewaiter.presentation.device.request;

import com.everyonewaiter.application.order.request.OrderWrite;
import com.everyonewaiter.domain.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderWriteRequest {

  @Schema(name = "OrderWriteRequest.Create")
  public record Create(
      @Schema(description = "테이블 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "테이블 번호가 누락되었습니다.")
      @Min(value = 0, message = "테이블 번호는 0 이상으로 입력해 주세요.")
      @Max(value = 100, message = "테이블 번호는 100 이하로 입력해 주세요.")
      Integer tableNo,

      @Schema(description = "주문 메모", example = "13시 포장", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메모 정보가 누락되었습니다.")
      @Size(max = 30, message = "주문 메모는 30자 이하로 입력해 주세요.")
      String memo,

      @Schema(description = "주문 메뉴", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 정보가 누락되었습니다.")
      @Size(min = 1, message = "장바구니에 메뉴를 1개 이상 담아 주세요.")
      List<@Valid CreateOrderMenu> orderMenus
  ) {

    public OrderWrite.Create toDomainDto(Order.Type type) {
      return new OrderWrite.Create(
          type,
          memo,
          orderMenus.stream()
              .map(CreateOrderMenu::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "OrderWriteRequest.CreateOrderMenu")
  public record CreateOrderMenu(
      @Schema(description = "메뉴 ID", example = "\"694865267482835533\"", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 ID가 누락되었습니다.")
      Long menuId,

      @Schema(description = "주문 수량", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 수량이 누락되었습니다.")
      @Min(value = 1, message = "주문 수량은 1 이상으로 입력해 주세요.")
      Integer quantity,

      @Schema(description = "주문 메뉴 옵션 그룹", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 옵션 그룹 정보가 누락되었습니다.")
      @Size(max = 20, message = "주문 메뉴 옵션 그룹은 20개 이하로 선택해 주세요.")
      List<@Valid CreateOrderOptionGroup> menuOptionGroups
  ) {

    public OrderWrite.OrderMenu toDomainDto() {
      return new OrderWrite.OrderMenu(
          menuId,
          quantity,
          menuOptionGroups.stream()
              .map(CreateOrderOptionGroup::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "OrderWriteRequest.CreateOrderOptionGroup")
  public record CreateOrderOptionGroup(
      @Schema(description = "메뉴 옵션 그룹 ID", example = "\"694865267482835533\"", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 그룹 ID가 누락되었습니다.")
      Long menuOptionGroupId,

      @Schema(description = "주문 메뉴 옵션", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메뉴 옵션 정보가 누락되었습니다.")
      @Size(max = 20, message = "주문 메뉴 옵션은 20개 이하로 선택해 주세요.")
      List<@Valid OrderOption> orderOptions
  ) {

    public OrderWrite.OptionGroup toDomainDto() {
      return new OrderWrite.OptionGroup(
          menuOptionGroupId,
          orderOptions.stream()
              .map(OrderOption::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "OrderWriteRequest.OrderOption")
  public record OrderOption(
      @Schema(description = "메뉴 옵션명", example = "밑반찬 주세요 O", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "메뉴 옵션명을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "메뉴 옵션명은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "메뉴 옵션 가격", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 가격 정보가 누락되었습니다.")
      @Min(value = -10_000_000, message = "메뉴 옵션 가격은 -10,000,000 이상으로 입력해 주세요.")
      @Max(value = 10_000_000, message = "메뉴 옵션 가격은 10,000,000 이하로 입력해 주세요.")
      long price
  ) {

    public OrderWrite.Option toDomainDto() {
      return new OrderWrite.Option(name, price);
    }

  }

}
