package com.everyonewaiter.global.sse;

public record SseEvent(
    Long storeId,
    SseCategory category,
    ServerAction action,
    boolean hasData,
    Object data
) {

  public SseEvent(Long storeId, SseCategory category, ServerAction action) {
    this(storeId, category, action, null);
  }

  public SseEvent(Long storeId, SseCategory category, ServerAction action, Object data) {
    this(storeId, category, action, data != null, data);
  }

}
