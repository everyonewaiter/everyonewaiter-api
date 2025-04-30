package com.everyonewaiter.application.store.response;

import com.everyonewaiter.domain.store.view.StoreSimpleView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "Store.SimpleResponses")
public record StoreSimpleResponses(List<StoreSimpleResponse> stores) {

  public static StoreSimpleResponses from(List<StoreSimpleView> views) {
    return new StoreSimpleResponses(
        views.stream()
            .map(StoreSimpleResponse::from)
            .toList()
    );
  }

  @Schema(name = "Store.SimpleResponse")
  public record StoreSimpleResponse(
      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "매장 이름", example = "홍길동식당")
      String name
  ) {

    public static StoreSimpleResponse from(StoreSimpleView view) {
      return new StoreSimpleResponse(view.id().toString(), view.name());
    }

  }

}
