package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "StoreSimpleResponses")
public record StoreSimpleResponses(List<StoreSimpleResponse> stores) {

  public static StoreSimpleResponses from(List<Store> stores) {
    return new StoreSimpleResponses(
        stores.stream()
            .map(StoreSimpleResponse::from)
            .toList()
    );
  }

}
