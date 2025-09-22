package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.application.notification.required.AlimTalkSender;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class NaverSensClientImpl implements AlimTalkSender {

  private final NaverSensClient naverSensClient;
  private final NaverSensProperties naverSensProperties;

  @Override
  public void send(AlimTalkMessage alimTalkMessage) {
    var request = new NaverSensAlimTalkRequest(naverSensProperties, alimTalkMessage);

    naverSensClient.send(naverSensProperties.getServiceId(), request);
  }

}
