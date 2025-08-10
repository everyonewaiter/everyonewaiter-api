package com.everyonewaiter.application.sse.provided;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseConnector {

  SseEmitter connect(String prefix, String lastEventId);

}
