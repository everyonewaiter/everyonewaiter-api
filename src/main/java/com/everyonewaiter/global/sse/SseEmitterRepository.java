package com.everyonewaiter.global.sse;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterRepository {

  void save(String key, SseEmitter sseEmitter);

  Map<String, SseEmitter> findAllByScanKey(String scanKey);

  void deleteByKey(String key);

}
