package com.everyonewaiter.application.sse;

import static com.everyonewaiter.domain.sse.SseKeyType.EMITTER;
import static com.everyonewaiter.domain.sse.SseKeyType.EVENT;
import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.sse.provided.SseConnector;
import com.everyonewaiter.application.sse.provided.SseSender;
import com.everyonewaiter.application.sse.required.SseEmitterRepository;
import com.everyonewaiter.application.sse.required.SseEventRepository;
import com.everyonewaiter.domain.sse.SseEvent;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
@RequiredArgsConstructor
class SseService implements SseConnector, SseSender {

  private static final Logger LOGGER = getLogger(SseService.class);

  private static final String CONNECT_EVENT = "CONNECTED!";

  private final JsonMapper jsonMapper;
  private final SseEmitterRepository sseEmitterRepository;
  private final SseEventRepository sseEventRepository;

  @Override
  public SseEmitter connect(String prefix, String lastEventId) {
    // SSE Emitter 생성
    String emitterKey = EMITTER.createKey(prefix);
    SseEmitter sseEmitter = createSseEmitter(emitterKey);
    sseEmitterRepository.save(emitterKey, sseEmitter);

    // 최초 연결 성공 메시지 전송 (503 에러 방지)
    send(sseEmitter, EVENT.createKey(prefix), CONNECT_EVENT);

    // 재연결인 경우 수신하지 못한 이벤트가 있는지 검색 후 전송
    if (StringUtils.hasText(lastEventId)) {
      sseEventRepository.findAllByScanKey(EVENT.createScanKey(prefix))
          .entrySet()
          .stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(entry -> send(sseEmitter, entry.getKey(), entry.getValue()));
    }

    return sseEmitter;
  }

  private SseEmitter createSseEmitter(String emitterKey) {
    SseEmitter sseEmitter = new SseEmitter(Duration.ofMinutes(5).toMillis());

    sseEmitter.onCompletion(() -> {
      LOGGER.debug("[SSE] Emitter completed. key: {}", emitterKey);
      sseEmitterRepository.deleteByKey(emitterKey);
    });
    sseEmitter.onTimeout(() -> {
      LOGGER.debug("[SSE] Emitter time out. key: {}", emitterKey);
      sseEmitter.complete();
    });
    sseEmitter.onError(throwable -> {
      LOGGER.debug("[SSE] Emitter error. key: {}, message={}", emitterKey, throwable.getMessage());
      sseEmitter.complete();
    });

    return sseEmitter;
  }

  @Override
  public void send(String prefix, SseEvent sseEvent) {
    LOGGER.info("[SSE 이벤트 전송] 매장 ID: {}, 카테고리: {}, 서버 활동: {}",
        sseEvent.storeId(), sseEvent.category(), sseEvent.action());
    LOGGER.info("[SSE 이벤트 본문] data: {}", sseEvent.data());

    String eventKey = EVENT.createKey(prefix);

    try {
      String event = jsonMapper.writeValueAsString(sseEvent);
      sseEventRepository.save(eventKey, event);

      sseEmitterRepository.findAllByScanKey(EMITTER.createScanKey(prefix))
          .values()
          .forEach(sseEmitter -> send(sseEmitter, eventKey, event));
    } catch (JacksonException exception) {
      LOGGER.warn("[SSE] Failed to serialize event. eventKey: {}", eventKey);
    }
  }

  private void send(SseEmitter sseEmitter, String key, String event) {
    try {
      sseEmitter.send(
          SseEmitter.event()
              .id(key)
              .name("sse")
              .data(event)
      );
    } catch (IOException | IllegalStateException exception) {
      sseEmitterRepository.deleteByKey(key);
    }
  }

}
