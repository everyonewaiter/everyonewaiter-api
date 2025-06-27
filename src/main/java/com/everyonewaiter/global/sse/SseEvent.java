package com.everyonewaiter.global.sse;

public record SseEvent(
    SseCategory category,
    ServerAction action,
    boolean hasResource,
    Object data
) {

  public SseEvent(SseCategory category, ServerAction action) {
    this(category, action, null);
  }

  public SseEvent(SseCategory category, ServerAction action, Object resource) {
    this(category, action, resource != null, resource);
  }

}
