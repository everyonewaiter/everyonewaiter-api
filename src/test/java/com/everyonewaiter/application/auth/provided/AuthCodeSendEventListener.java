package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.domain.auth.AuthCodeSendEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class AuthCodeSendEventListener {

  private final List<AuthCodeSendEvent> events = new ArrayList<>();

  @EventListener
  public void consumeAuthCodeSendEvent(AuthCodeSendEvent event) {
    events.add(event);
  }

  public List<AuthCodeSendEvent> getEvents() {
    return Collections.unmodifiableList(events);
  }

  public void clear() {
    events.clear();
  }

}
