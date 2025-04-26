package com.everyonewaiter.application.notification.alimtalk.service;

import com.everyonewaiter.domain.notification.alimtalk.AlimTalkMessage;
import java.util.List;

public interface AlimTalkClient {

  void sendAlimTalk(String templateCode, AlimTalkMessage messages);

  void sendAlimTalk(String templateCode, List<AlimTalkMessage> messages);

}
