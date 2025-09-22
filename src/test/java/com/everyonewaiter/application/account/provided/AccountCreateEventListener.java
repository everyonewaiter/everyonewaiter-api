package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.AccountCreateEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class AccountCreateEventListener {

  private final List<AccountCreateEvent> events = new ArrayList<>();

  @EventListener
  public void consumeAccountCreateEvent(AccountCreateEvent event) {
    events.add(event);
  }

  public List<AccountCreateEvent> getEvents() {
    return Collections.unmodifiableList(events);
  }

  public void clear() {
    events.clear();
  }

}
