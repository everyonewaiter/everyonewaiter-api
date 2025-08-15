package com.everyonewaiter.application.store;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.domain.store.event.LicenseImageDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class LicenseImageDeleteEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LicenseImageDeleteEventHandler.class);

  private final ImageManager imageManager;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(LicenseImageDeleteEvent event) {
    LOGGER.info("[사업자 등록증 이미지 삭제] {}: {}", event.storeName(), event.licenseImage());
    imageManager.delete(event.licenseImage());
  }

}
