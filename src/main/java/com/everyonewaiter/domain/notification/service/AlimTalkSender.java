package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlimTalkSender {

  private final AlimTalkClient alimTalkClient;

  public void sendTo(String templateCode, AlimTalkMessage messages) {
    alimTalkClient.send(templateCode, messages);
  }

}
