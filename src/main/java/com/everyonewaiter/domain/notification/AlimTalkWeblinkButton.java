package com.everyonewaiter.domain.notification;

public record AlimTalkWeblinkButton(
    String type,
    String name,
    String linkMobile,
    String linkPc
) implements AlimTalkButton {

  public AlimTalkWeblinkButton(String name, String link) {
    this("WL", name, link, link);
  }

}
