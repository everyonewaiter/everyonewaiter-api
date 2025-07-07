package com.everyonewaiter.global.sse;

import java.util.Objects;

public record SseEvent(
    String storeId,
    SseCategory category,
    ServerAction action,
    boolean hasData,
    Object data
) {

  public SseEvent(Long storeId, SseCategory category, ServerAction action) {
    this(storeId, category, action, null);
  }

  public SseEvent(Long storeId, SseCategory category, ServerAction action, Object data) {
    this(Objects.requireNonNull(storeId).toString(), category, action, data != null, data);
  }

}
