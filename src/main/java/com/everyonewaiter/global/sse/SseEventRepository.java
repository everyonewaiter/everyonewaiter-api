package com.everyonewaiter.global.sse;

import java.util.Map;

public interface SseEventRepository {

  void save(String key, String event);

  Map<String, String> findAllByScanKey(String scanKey);

}
