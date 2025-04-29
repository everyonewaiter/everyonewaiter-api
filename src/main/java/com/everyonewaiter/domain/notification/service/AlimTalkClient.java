package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import java.util.List;

public interface AlimTalkClient {

  void send(String templateCode, AlimTalkMessage messages);

  void send(String templateCode, List<AlimTalkMessage> messages);

}
