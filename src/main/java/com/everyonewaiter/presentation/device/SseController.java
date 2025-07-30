package com.everyonewaiter.presentation.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.sse.SseService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class SseController implements SseControllerSpecification {

  private final SseService sseService;

  @Override
  @GetMapping(value = "/stores/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> subscribe(
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
      @AuthenticationDevice Device device,
      HttpServletResponse response
  ) {
    response.setHeader(HttpHeaders.CONNECTION, "keep-alive");
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    response.setHeader("X-Accel-Buffering", "no");
    return ResponseEntity.ok(sseService.connect(
        Objects.requireNonNull(device.getStore().getId()).toString(), lastEventId)
    );
  }

}
