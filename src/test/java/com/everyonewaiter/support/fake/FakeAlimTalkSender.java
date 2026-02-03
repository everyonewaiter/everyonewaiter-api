package com.everyonewaiter.support.fake;

import com.everyonewaiter.application.notification.required.AlimTalkSender;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeAlimTalkSender implements AlimTalkSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(FakeAlimTalkSender.class);

  private final List<AlimTalkMessage> sentMessages = new ArrayList<>();

  @Override
  public void send(AlimTalkMessage alimTalkMessage) {
    LOGGER.info("[FAKE 알림톡 발송] templateCode: {}, to: {}, content: {}, buttons: {}",
        alimTalkMessage.getTemplateCode(),
        alimTalkMessage.getTo(),
        alimTalkMessage.getContent(),
        alimTalkMessage.getButtons()
    );
    sentMessages.add(alimTalkMessage);
  }

  public void clear() {
    sentMessages.clear();
  }

  public List<AlimTalkMessage> getSentMessages() {
    return Collections.unmodifiableList(sentMessages);
  }

}
