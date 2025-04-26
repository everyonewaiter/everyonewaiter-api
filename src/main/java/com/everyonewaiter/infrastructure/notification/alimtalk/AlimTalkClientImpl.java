package com.everyonewaiter.infrastructure.notification.alimtalk;

import com.everyonewaiter.application.notification.alimtalk.service.AlimTalkClient;
import com.everyonewaiter.domain.notification.alimtalk.AlimTalkMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AlimTalkClientImpl implements AlimTalkClient {

  private final NaverSensClient naverSensClient;
  private final NaverSensProperties naverSensProperties;

  @Override
  public void sendAlimTalk(String templateCode, AlimTalkMessage messages) {
    sendAlimTalk(templateCode, List.of(messages));
  }

  @Override
  public void sendAlimTalk(String templateCode, List<AlimTalkMessage> messages) {
    NaverSensAlimTalkRequest request =
        new NaverSensAlimTalkRequest(templateCode, naverSensProperties.getChannelId(), messages);
    naverSensClient.sendAlimTalk(naverSensProperties.getServiceId(), request);
  }

}
