package com.everyonewaiter.adapter.web.api.device;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import com.everyonewaiter.application.sse.provided.SseConnector;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class SseApi implements SseApiSpecification {

  private final SseConnector sseConnector;

  @Override
  @GetMapping(value = "/stores/subscribe", produces = TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> connect(
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
      @AuthenticationDevice Device device,
      HttpServletResponse response
  ) {
    configureSseResponseHeader(response);

    SseEmitter sseEmitter = sseConnector.connect(device.getStoreId().toString(), lastEventId);

    return ResponseEntity.ok(sseEmitter);
  }

  private void configureSseResponseHeader(HttpServletResponse response) {
    response.setHeader(CONTENT_TYPE, "text/event-stream");
    response.setHeader(CONNECTION, "keep-alive");
    response.setHeader(CACHE_CONTROL, "no-cache");
    response.setHeader("X-Accel-Buffering", "no");
  }

}
