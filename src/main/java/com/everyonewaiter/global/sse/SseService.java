package com.everyonewaiter.global.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SseService.class);
  private static final String KEY_DELIMITER = ":";
  private static final String CONNECT_EVENT = "CONNECTED!";

  private final ObjectMapper objectMapper;
  private final SseEmitterRepository sseEmitterRepository;
  private final SseEventRepository sseEventRepository;

  public SseEmitter connect(String prefix, String lastEventId) {
    String key = generateKey(prefix, SseKeyType.EMITTER);
    SseEmitter sseEmitter = createSseEmitter(key);
    sseEmitterRepository.save(key, sseEmitter);

    sendEvent(sseEmitter, prefix, CONNECT_EVENT);
    if (StringUtils.hasText(lastEventId)) {
      sseEventRepository.findAllByScanKey(generateScanKey(prefix, SseKeyType.EVENT))
          .entrySet()
          .stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(entry -> sendEvent(sseEmitter, entry.getKey(), entry.getValue()));
    }

    return sseEmitter;
  }

  private SseEmitter createSseEmitter(String key) {
    SseEmitter sseEmitter = new SseEmitter(Duration.ofMinutes(5).toMillis());

    sseEmitter.onCompletion(() -> {
      LOGGER.debug("[SSE] Emitter completed. key: {}", key);
      sseEmitterRepository.deleteByKey(key);
    });
    sseEmitter.onTimeout(() -> {
      LOGGER.debug("[SSE] Emitter time out. key: {}", key);
      sseEmitter.complete();
    });
    sseEmitter.onError(throwable -> {
      LOGGER.debug("[SSE] Emitter error. key: {}", key, throwable);
      sseEmitter.completeWithError(throwable);
    });

    return sseEmitter;
  }

  public void sendEvent(String prefix, SseEvent event) {
    String key = generateKey(prefix, SseKeyType.EVENT);
    try {
      String stringEvent = objectMapper.writeValueAsString(event);
      sseEventRepository.save(key, stringEvent);
      sseEmitterRepository.findAllByScanKey(generateScanKey(prefix, SseKeyType.EMITTER))
          .values()
          .forEach(sseEmitter -> sendEvent(sseEmitter, key, stringEvent));
    } catch (IOException exception) {
      LOGGER.debug("[SSE] Failed to serialize event. key: {}", key);
    }
  }

  private void sendEvent(SseEmitter sseEmitter, String key, String event) {
    try {
      sseEmitter.send(
          SseEmitter.event()
              .id(key)
              .name("sse")
              .data(event)
      );
    } catch (IOException exception) {
      LOGGER.debug("[SSE] Failed to send event. key: {}", key);
    }
  }

  private String generateKey(String prefix, SseKeyType sseKeyType) {
    return generateScanKey(prefix, sseKeyType) + System.currentTimeMillis();
  }

  private String generateScanKey(String prefix, SseKeyType sseKeyType) {
    return prefix + KEY_DELIMITER + sseKeyType.getLowerCaseName() + KEY_DELIMITER;
  }

}
