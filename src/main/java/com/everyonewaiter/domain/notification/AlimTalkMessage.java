package com.everyonewaiter.domain.notification;

import java.util.List;

public record AlimTalkMessage(
    String to,
    String content,
    boolean useSmsFailover,
    List<AlimTalkButton> buttons
) {

  public AlimTalkMessage(String to, String content) {
    this(to, content, List.of());
  }

  public AlimTalkMessage(String to, String content, List<AlimTalkButton> buttons) {
    this(to, content, true, buttons);
  }

}
