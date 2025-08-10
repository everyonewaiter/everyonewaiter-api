package com.everyonewaiter.application.sse.required;

import java.util.Map;

public interface SseEventRepository {

  String save(String key, String event);

  Map<String, String> findAllByScanKey(String scanKey);

}
