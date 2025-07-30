package com.everyonewaiter.application.order.response;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.OrderOption;
import com.everyonewaiter.domain.order.entity.OrderOptionGroup;
import com.everyonewaiter.domain.order.entity.StaffCall;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

  @Schema(name = "OrderResponse.Details")
  public record Details(List<Detail> orders) {

    public static Details from(List<Order> orders) {
      return new Details(
          orders.stream()
              .map(Detail::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderResponse.Detail")
  public record Detail(
      @Schema(description = "주문 ID", example = "\"694865267482835533\"")
      String orderId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "주문 카테고리 (첫 주문, 추가 주문)", example = "INITIAL")
      Order.Category category,

      @Schema(description = "주문 타입", example = "POSTPAID")
      Order.Type type,

      @Schema(description = "주문 상태", example = "ORDER")
      Order.State state,

      @Schema(description = "주문 금액", example = "34900")
      long price,

      @Schema(description = "주문 메모", example = "13시 포장")
      String memo,

      @Schema(description = "주문 서빙 여부", example = "false")
      boolean served,

      @Schema(description = "주문 서빙 시간", example = "2025-01-01 12:00:00")
      Instant servedTime,

      @Schema(description = "주문 메뉴 목록")
      List<OrderMenuDetail> orderMenus,

      @Schema(description = "주문 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "주문 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static Detail from(Order order) {
      return new Detail(
          Objects.requireNonNull(order.getId()).toString(),
          Objects.requireNonNull(order.getStore().getId()).toString(),
          order.getCategory(),
          order.getType(),
          order.getState(),
          order.getPrice(),
          order.getMemo(),
          order.getServing().isServed(),
          order.getServing().getServedTime(),
          order.getOrderMenus()
              .stream()
              .map(OrderMenuDetail::from)
              .toList(),
          order.getCreatedAt(),
          order.getUpdatedAt()
      );
    }

  }

  @Schema(name = "OrderResponse.OrderMenuDetail")
  public record OrderMenuDetail(
      @Schema(description = "주문 메뉴 ID", example = "\"694865267482835533\"")
      String orderMenuId,

      @Schema(description = "주문 메뉴 이름", example = "안심 스테이크")
      String name,

      @Schema(description = "주문 메뉴 가격", example = "34900")
      long price,

      @Schema(description = "주문 메뉴 수량", example = "1")
      int quantity,

      @Schema(description = "주문 메뉴 서빙 여부", example = "false")
      boolean served,

      @Schema(description = "주문 메뉴 서빙 시간", example = "2025-01-01 12:00:00")
      Instant servedTime,

      @Schema(description = "주문 메뉴 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "주문 메뉴 옵션 그룹 목록")
      List<OptionGroup> orderOptionGroups
  ) {

    public static OrderMenuDetail from(OrderMenu orderMenu) {
      return new OrderMenuDetail(
          Objects.requireNonNull(orderMenu.getId()).toString(),
          orderMenu.getName(),
          orderMenu.getPrice(),
          orderMenu.getQuantity(),
          orderMenu.getServing().isServed(),
          orderMenu.getServing().getServedTime(),
          orderMenu.isPrintEnabled(),
          orderMenu.getOrderOptionGroups()
              .stream()
              .map(OptionGroup::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderResponse.OptionGroup")
  public record OptionGroup(
      @Schema(description = "주문 메뉴 옵션 그룹 ID", example = "\"694865267482835533\"")
      String orderOptionGroupId,

      @Schema(description = "주문 메뉴 옵션 그룹명", example = "굽기 정도")
      String name,

      @Schema(description = "주문 메뉴 옵션 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "주문 메뉴 옵션 목록")
      List<Option> orderOptions
  ) {

    public static OptionGroup from(OrderOptionGroup orderOptionGroup) {
      return new OptionGroup(
          Objects.requireNonNull(orderOptionGroup.getId()).toString(),
          orderOptionGroup.getName(),
          orderOptionGroup.isPrintEnabled(),
          orderOptionGroup.getOrderOptions()
              .stream()
              .map(Option::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderResponse.Option")
  public record Option(
      @Schema(description = "주문 메뉴 옵션명", example = "미디움")
      String name,

      @Schema(description = "주문 메뉴 옵션 가격", example = "0")
      long price
  ) {

    public static Option from(OrderOption orderOption) {
      return new Option(
          orderOption.getName(),
          orderOption.getPrice()
      );
    }

  }

  @Schema(name = "OrderResponse.StaffCallDetails")
  public record StaffCallDetails(List<StaffCallDetail> staffCalls) {

    public static StaffCallDetails from(List<StaffCall> staffCalls) {
      return new StaffCallDetails(
          staffCalls.stream()
              .map(StaffCallDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderResponse.StaffCallDetail")
  public record StaffCallDetail(
      @Schema(description = "직원 호출 ID", example = "\"694865267482835533\"")
      String staffCallId,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "직원 호출 옵션명", example = "직원 호출")
      String name,

      @Schema(description = "직원 호출 상태", example = "INCOMPLETE")
      StaffCall.State state,

      @Schema(description = "직원 호출 완료 시간", example = "1970-01-01 00:00:00")
      Instant completeTime,

      @Schema(description = "직원 호출 시간", example = "2025-01-01 12:00:00")
      Instant createdAt
  ) {

    public static StaffCallDetail from(StaffCall staffCall) {
      return new StaffCallDetail(
          Objects.requireNonNull(staffCall.getId()).toString(),
          staffCall.getTableNo(),
          staffCall.getName(),
          staffCall.getState(),
          staffCall.getCompleteTime(),
          staffCall.getCreatedAt()
      );
    }

  }

}
