package com.everyonewaiter.domain.pos;

import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderPaymentView;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class PosView {

  @Schema(name = "PosView.Revenue")
  public record Revenue(
      @Schema(description = "총 주문 금액", example = "29900")
      long totalOrderPrice,

      @Schema(description = "총 할인 금액", example = "0")
      long totalDiscountPrice,

      @Schema(description = "총 결제 금액", example = "29900")
      long totalPaymentPrice,

      @Schema(description = "현금 결제 승인 금액", example = "0")
      long cashPaymentApprovePrice,

      @Schema(description = "카드 결제 승인 금액", example = "29900")
      long cardPaymentApprovePrice,

      @Schema(description = "현금 결제 취소 금액", example = "0")
      long cashPaymentCancelPrice,

      @Schema(description = "카드 결제 취소 금액", example = "0")
      long cardPaymentCancelPrice
  ) {

    public static Revenue from(
        long totalOrderedOrderPrice,
        long totalDiscountPrice,
        long cashPaymentApprovePrice,
        long cardPaymentApprovePrice,
        long cashPaymentCancelPrice,
        long cardPaymentCancelPrice
    ) {
      long totalPaymentApprovePrice = cashPaymentApprovePrice + cardPaymentApprovePrice;
      long totalPaymentCancelPrice = cashPaymentCancelPrice + cardPaymentCancelPrice;

      return new Revenue(
          totalOrderedOrderPrice,
          totalDiscountPrice,
          totalPaymentApprovePrice - totalPaymentCancelPrice,
          cashPaymentApprovePrice,
          cardPaymentApprovePrice,
          cashPaymentCancelPrice,
          cardPaymentCancelPrice
      );
    }

  }

  @Schema(name = "PosView.PosTableDetail")
  public record PosTableDetail(
      @Schema(description = "POS 테이블 ID", example = "\"694865267482835533\"")
      String posTableId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "주문 존재 여부", example = "true")
      boolean hasOrder,

      @Schema(description = "승인 대기 주문 존재 여부", example = "true")
      boolean hasPendingOrder,

      @Nullable
      @Schema(description = "주문 타입", example = "POSTPAID")
      OrderType orderType,

      @Nullable
      @Schema(description = "주문 시간", example = "2025-01-01 12:00:00")
      Instant orderedAt,

      @Nullable
      @Schema(description = "주문한 메뉴명", example = "오일 파스타")
      String orderMenuName,

      @Schema(description = "주문한 메뉴 건수", example = "3")
      int orderMenuCount,

      @Schema(description = "총 주문 금액", example = "29900")
      long totalOrderPrice,

      @Schema(description = "할인 금액", example = "0")
      long discount
  ) {

    public static PosTableDetail from(PosTable posTable) {
      return new PosTableDetail(
          String.valueOf(posTable.getId()),
          String.valueOf(posTable.getStore().getId()),
          posTable.getTableNo(),
          posTable.hasOrder(),
          posTable.hasPendingOrder(),
          posTable.getTableOrderType().orElse(null),
          posTable.getActivityCreatedAt().orElse(null),
          posTable.getFirstOrderMenuName().orElse(null),
          posTable.getOrderMenuCount(),
          posTable.getTableTotalOrderPrice(),
          posTable.getDiscountPrice()
      );
    }

  }

  @Schema(name = "PosView.PosTableActivityDetail")
  public record PosTableActivityDetail(
      @Schema(description = "POS 테이블 액티비티 ID", example = "\"694865267482835533\"")
      String posTableActivityId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "POS 테이블 ID", example = "\"694865267482835533\"")
      String posTableId,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "테이블 결제 타입", example = "POSTPAID")
      OrderType orderType,

      @Schema(description = "승인 대기 주문 존재 여부", example = "true")
      boolean hasPendingOrder,

      @Schema(description = "총 주문 금액", example = "10000")
      long totalOrderPrice,

      @Schema(description = "총 결제 금액", example = "0")
      long totalPaymentPrice,

      @Schema(description = "할인 금액", example = "0")
      long discount,

      @Schema(description = "잔여 결제 금액", example = "0")
      long remainingPaymentPrice,

      @Schema(description = "POS 테이블 액티비티 활성화 여부", example = "true")
      boolean active,

      @Schema(description = "주문 목록 (승인 대기 주문이 존재하는 경우 승인 대기 주문 목록)")
      List<OrderView.OrderDetail> orders,

      @Schema(description = "주문 결제 목록")
      List<OrderPaymentView.OrderPaymentDetail> orderPayments
  ) {

    public static PosTableActivityDetail from(PosTableActivity posTableActivity) {
      boolean hasPendingOrder = posTableActivity.hasPendingOrder();
      List<Order> orders = hasPendingOrder
          ? posTableActivity.getPendingOrders()
          : posTableActivity.getOrderedOrders();

      return new PosTableActivityDetail(
          String.valueOf(posTableActivity.getId()),
          String.valueOf(posTableActivity.getStore().getId()),
          String.valueOf(posTableActivity.getPosTable().getId()),
          posTableActivity.getTableNo(),
          posTableActivity.getTableOrderType(),
          hasPendingOrder,
          posTableActivity.getTotalOrderPrice(),
          posTableActivity.getTotalPaymentPrice(),
          posTableActivity.getDiscount(),
          posTableActivity.getRemainingPaymentPriceWithDiscount(),
          posTableActivity.isActive(),
          orders.stream()
              .map(OrderView.OrderDetail::from)
              .toList(),
          posTableActivity.getPayments().stream()
              .map(OrderPaymentView.OrderPaymentDetail::from)
              .toList()
      );
    }

  }

}
