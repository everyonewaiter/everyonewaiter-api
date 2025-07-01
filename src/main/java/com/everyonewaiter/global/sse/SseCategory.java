package com.everyonewaiter.global.sse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseCategory {
  DEVICE,
  STORE,
  CATEGORY,
  MENU,
  WAITING,
  ORDER,
  STAFF_CALL,
  RECEIPT,
  POS,
}
