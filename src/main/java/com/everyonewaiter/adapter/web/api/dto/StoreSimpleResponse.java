package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.Store;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StoreSimpleResponse")
public record StoreSimpleResponse(
    @Schema(description = "매장 ID", example = "\"694865267482835533\"")
    String storeId,

    @Schema(description = "매장 이름", example = "홍길동식당")
    String name
) {

  public static StoreSimpleResponse from(Store store) {
    return new StoreSimpleResponse(String.valueOf(store.getId()), store.getDetail().getName());
  }

}
