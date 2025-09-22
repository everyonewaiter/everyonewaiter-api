package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.domain.auth.AuthMailSendEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class AuthMailSendEventListener {

  private final List<AuthMailSendEvent> events = new ArrayList<>();

  @EventListener
  public void consumeAuthMailSendEvent(AuthMailSendEvent event) {
    events.add(event);
  }

  public List<AuthMailSendEvent> getEvents() {
    return Collections.unmodifiableList(events);
  }

  public void clear() {
    events.clear();
  }

}
