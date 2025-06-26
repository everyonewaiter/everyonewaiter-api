package com.everyonewaiter.global.sse;

enum SseKeyType {
  EMITTER,
  EVENT,
  ;

  public String getLowerCaseName() {
    return name().toLowerCase();
  }

}
