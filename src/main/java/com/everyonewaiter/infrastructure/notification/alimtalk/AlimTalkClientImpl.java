package com.everyonewaiter.infrastructure.notification.alimtalk;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.service.AlimTalkClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AlimTalkClientImpl implements AlimTalkClient {

  private final NaverSensClient naverSensClient;
  private final NaverSensProperties naverSensProperties;

  @Override
  public void send(String templateCode, AlimTalkMessage messages) {
    send(templateCode, List.of(messages));
  }

  @Override
  public void send(String templateCode, List<AlimTalkMessage> messages) {
    NaverSensAlimTalkRequest request =
        new NaverSensAlimTalkRequest(templateCode, naverSensProperties.getChannelId(), messages);
    naverSensClient.send(naverSensProperties.getServiceId(), request);
  }

}
