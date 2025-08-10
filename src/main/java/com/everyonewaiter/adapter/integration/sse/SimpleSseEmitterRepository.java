package com.everyonewaiter.adapter.integration.sse;

import com.everyonewaiter.application.sse.required.SseEmitterRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
class SimpleSseEmitterRepository implements SseEmitterRepository {

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter save(String key, SseEmitter sseEmitter) {
    emitters.put(key, sseEmitter);

    return sseEmitter;
  }

  @Override
  public Map<String, SseEmitter> findAllByScanKey(String scanKey) {
    return emitters.entrySet()
        .stream()
        .filter(entry -> entry.getKey().startsWith(scanKey))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public void deleteByKey(String key) {
    emitters.remove(key);
  }

}
