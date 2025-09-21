package com.everyonewaiter.domain.order;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class OrderView {

  @Schema(name = "OrderView.OrderDetail")
  public record OrderDetail(
      @Schema(description = "주문 ID", example = "\"694865267482835533\"")
      String orderId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "주문 카테고리 (첫 주문, 추가 주문)", example = "INITIAL")
      OrderCategory category,

      @Schema(description = "주문 타입", example = "POSTPAID")
      OrderType type,

      @Schema(description = "주문 상태", example = "ORDER")
      OrderState state,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

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

    public static OrderDetail from(Order order) {
      return new OrderDetail(
          String.valueOf(order.getId()),
          String.valueOf(order.getStore().getId()),
          order.getCategory(),
          order.getType(),
          order.getState(),
          order.getPosTableActivity().getTableNo(),
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

  @Schema(name = "OrderView.OrderMenuDetail")
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
      List<OrderOptionGroupDetail> orderOptionGroups
  ) {

    public static OrderMenuDetail from(OrderMenu orderMenu) {
      return new OrderMenuDetail(
          String.valueOf(orderMenu.getId()),
          orderMenu.getName(),
          orderMenu.getPrice(),
          orderMenu.getQuantity(),
          orderMenu.getServing().isServed(),
          orderMenu.getServing().getServedTime(),
          orderMenu.isPrintEnabled(),
          orderMenu.getOrderOptionGroups()
              .stream()
              .map(OrderOptionGroupDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderView.OrderOptionGroupDetail")
  public record OrderOptionGroupDetail(
      @Schema(description = "주문 메뉴 옵션 그룹 ID", example = "\"694865267482835533\"")
      String orderOptionGroupId,

      @Schema(description = "주문 메뉴 옵션 그룹명", example = "굽기 정도")
      String name,

      @Schema(description = "주문 메뉴 옵션 주방 프린트 출력 여부", example = "true")
      boolean printEnabled,

      @Schema(description = "주문 메뉴 옵션 목록")
      List<OrderOptionDetail> orderOptions
  ) {

    public static OrderOptionGroupDetail from(OrderOptionGroup orderOptionGroup) {
      return new OrderOptionGroupDetail(
          String.valueOf(orderOptionGroup.getId()),
          orderOptionGroup.getName(),
          orderOptionGroup.isPrintEnabled(),
          orderOptionGroup.getOrderOptions()
              .stream()
              .map(OrderOptionDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "OrderView.OrderOptionDetail")
  public record OrderOptionDetail(
      @Schema(description = "주문 메뉴 옵션명", example = "미디움")
      String name,

      @Schema(description = "주문 메뉴 옵션 가격", example = "0")
      long price
  ) {

    public static OrderOptionDetail from(OrderOption orderOption) {
      return new OrderOptionDetail(orderOption.getName(), orderOption.getPrice());
    }

  }

}
