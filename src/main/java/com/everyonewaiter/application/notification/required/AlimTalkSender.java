package com.everyonewaiter.application.notification.required;

import com.everyonewaiter.domain.notification.AlimTalkMessage;

public interface AlimTalkSender {

  void send(AlimTalkMessage alimTalkMessage);

}
