package com.everyonewaiter.application.sse.required;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterRepository {

  SseEmitter save(String key, SseEmitter sseEmitter);

  Map<String, SseEmitter> findAllByScanKey(String scanKey);

  void deleteByKey(String key);

}
