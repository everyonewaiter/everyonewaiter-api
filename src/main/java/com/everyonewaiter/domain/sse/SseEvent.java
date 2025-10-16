package com.everyonewaiter.domain.sse;

import jakarta.annotation.Nullable;

public record SseEvent(
    String storeId,
    SseCategory category,
    ServerAction action,
    boolean hasData,
    @Nullable Object data
) {

  public SseEvent(Long storeId, SseCategory category, ServerAction action) {
    this(storeId, category, action, null);
  }

  public SseEvent(Long storeId, SseCategory category, ServerAction action, @Nullable Object data) {
    this(String.valueOf(storeId), category, action, data != null, data);
  }

}
