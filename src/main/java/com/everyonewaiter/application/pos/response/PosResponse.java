package com.everyonewaiter.application.pos.response;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.pos.entity.PosTable;
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
      Order.Type orderType,

      @Nullable
      @Schema(description = "주문 시간", example = "2025-01-01 12:00:00")
      Instant orderedAt,

      @Schema(description = "총 주문 금액", example = "29900")
      long totalOrderPrice,

      @Nullable
      @Schema(description = "주문한 메뉴명", example = "오일 파스타")
      String orderMenuName,

      @Schema(description = "주문한 메뉴 건수", example = "3")
      int orderMenuCount
  ) {

    public static Table from(PosTable posTable) {
      return new Table(
          Objects.requireNonNull(posTable.getId()).toString(),
          Objects.requireNonNull(posTable.getStore().getId()).toString(),
          posTable.getName(),
          posTable.getTableNo(),
          posTable.hasOrder(),
          posTable.getTablePaymentType(),
          posTable.getLastActivityTime(),
          posTable.getTotalOrderPrice(),
          posTable.getFirstOrderMenuName(),
          posTable.getOrderMenuCount()
      );
    }

  }

}
