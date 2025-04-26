package com.everyonewaiter.infrastructure.notification.alimtalk;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "naverSensClient",
    url = "https://sens.apigw.ntruss.com",
    configuration = NaverSensProperties.class
)
interface NaverSensClient {

  @PostMapping(value = "/alimtalk/v2/services/{serviceId}/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
  void sendAlimTalk(
      @PathVariable String serviceId,
      @RequestBody NaverSensAlimTalkRequest request
  );

}
