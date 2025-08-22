package com.everyonewaiter.application.pos.response;

import com.everyonewaiter.domain.order.OrderPaymentView;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderView;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.view.PosTableActivityView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PosResponse {

  @Schema(name = "PosResponse.Tables")
  public record Tables(List<Table> tables) {

    public static Tables from(List<PosTable> posTables) {
      return new Tables(
          posTables.stream()
              .map(Table::from)
              .toList()
      );
    }

  }

  @Schema(name = "PosResponse.Table")
  public record Table(
      @Schema(description = "POS 테이블 ID", example = "\"694865267482835533\"")
      String posTableId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "테이블명", example = "T-1")
      String name,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "주문 존재 여부", example = "true")
      boolean hasOrder,

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

    public static Table from(PosTable posTable) {
      return new Table(
          Objects.requireNonNull(posTable.getId()).toString(),
          Objects.requireNonNull(posTable.getStore().getId()).toString(),
          posTable.getName(),
          posTable.getTableNo(),
          posTable.hasActiveOrder(),
          posTable.getActiveTablePaymentType().orElse(null),
          posTable.getActiveActivityTime().orElse(null),
          posTable.getFirstOrderMenuName().orElse(null),
          posTable.getActiveOrderMenuCount(),
          posTable.getActiveTotalOrderPrice(),
          posTable.getActiveDiscountPrice()
      );
    }

  }

  @Schema(name = "PosResponse.TableActivityDetail")
  public record TableActivityDetail(
      @Schema(description = "POS 테이블 액티비티 ID", example = "\"694865267482835533\"")
      String posTableActivityId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "POS 테이블 ID", example = "\"694865267482835533\"")
      String posTableId,

      @Schema(description = "테이블명", example = "T-1")
      String name,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "테이블 결제 타입", example = "POSTPAID")
      OrderType orderType,

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

      @Schema(description = "주문 목록")
      List<OrderView.OrderDetail> orders,

      @Schema(description = "주문 결제 목록")
      List<OrderPaymentView.OrderPaymentDetail> orderPayments
  ) {

    public static TableActivityDetail from(PosTableActivity posTableActivity) {
      return new TableActivityDetail(
          Objects.requireNonNull(posTableActivity.getId()).toString(),
          Objects.requireNonNull(posTableActivity.getStore().getId()).toString(),
          Objects.requireNonNull(posTableActivity.getPosTable().getId()).toString(),
          posTableActivity.getPosTable().getName(),
          posTableActivity.getPosTable().getTableNo(),
          posTableActivity.getTablePaymentType(),
          posTableActivity.getTotalOrderPrice(),
          posTableActivity.getTotalPaymentPrice(),
          posTableActivity.getDiscount(),
          posTableActivity.getRemainingPaymentPriceWithDiscount(),
          posTableActivity.isActive(),
          posTableActivity.getOrderedOrders().stream()
              .map(OrderView.OrderDetail::from)
              .toList(),
          posTableActivity.getPayments().stream()
              .map(OrderPaymentView.OrderPaymentDetail::from)
              .toList()
      );
    }

  }

  @Schema(name = "PosResponse.Revenue")
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
        PosTableActivityView.TotalRevenue totalRevenueView,
        long cashPaymentApprovePrice,
        long cardPaymentApprovePrice,
        long cashPaymentCancelPrice,
        long cardPaymentCancelPrice
    ) {
      return new Revenue(
          totalRevenueView.totalOrderPrice(),
          totalRevenueView.totalDiscountPrice(),
          totalRevenueView.totalPaymentPrice(),
          cashPaymentApprovePrice,
          cardPaymentApprovePrice,
          cashPaymentCancelPrice,
          cardPaymentCancelPrice
      );
    }

  }

}
