package com.everyonewaiter.application.store.response;

import com.everyonewaiter.domain.store.view.StoreSimpleView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponse {

  @Schema(name = "StoreResponse.Simples")
  public record Simples(List<Simple> stores) {

    public static Simples from(List<StoreSimpleView> views) {
      return new Simples(
          views.stream()
              .map(Simple::from)
              .toList()
      );
    }

    @Schema(name = "StoreResponse.Simple")
    public record Simple(
        @Schema(description = "매장 ID", example = "\"694865267482835533\"")
        String storeId,

        @Schema(description = "매장 이름", example = "홍길동식당")
        String name
    ) {

      public static Simple from(StoreSimpleView view) {
        return new Simple(view.id().toString(), view.name());
      }

    }

  }

}
