package com.everyonewaiter.application.notification.alimtalk.service;

import com.everyonewaiter.application.notification.alimtalk.service.request.AlimTalkSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlimTalkService {

  private final AlimTalkClient alimTalkClient;

  public void sendAlimTalk(AlimTalkSend request) {
    alimTalkClient.sendAlimTalk(request.templateCode(), request.messages());
  }

}
