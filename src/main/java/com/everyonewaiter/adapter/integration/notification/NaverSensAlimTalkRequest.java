package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import java.util.List;

record NaverSensAlimTalkRequest(
    String templateCode,
    String plusFriendId,
    List<AlimTalkMessage> messages
) {

  NaverSensAlimTalkRequest(NaverSensProperties properties, AlimTalkMessage message) {
    this(message.getTemplateCode(), properties.getChannelId(), List.of(message));
  }

}
