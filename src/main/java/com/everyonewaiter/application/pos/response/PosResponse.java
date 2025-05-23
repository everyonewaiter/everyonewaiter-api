package com.everyonewaiter.application.pos.response;

import com.everyonewaiter.domain.pos.entity.PosTable;
import io.swagger.v3.oas.annotations.media.Schema;
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
      int tableNo
  ) {

    public static Table from(PosTable posTable) {
      return new Table(
          Objects.requireNonNull(posTable.getId()).toString(),
          posTable.getStoreId().toString(),
          posTable.getName(),
          posTable.getTableNo()
      );
    }

  }

}
