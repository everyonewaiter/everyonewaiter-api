package com.everyonewaiter.domain.sse;

public enum SseKeyType {

  EMITTER,
  EVENT,
  ;

  private static final String KEY_DELIMITER = ":";

  public String createKey(String prefix) {
    return createScanKey(prefix) + System.currentTimeMillis();
  }

  public String createScanKey(String prefix) {
    return prefix + KEY_DELIMITER + getLowerCaseName() + KEY_DELIMITER;
  }

  private String getLowerCaseName() {
    return name().toLowerCase();
  }

}
