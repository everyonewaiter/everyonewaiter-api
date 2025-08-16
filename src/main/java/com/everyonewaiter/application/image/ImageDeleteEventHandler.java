package com.everyonewaiter.application.image;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.domain.store.LicenseImageDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class ImageDeleteEventHandler {

  private static final Logger LOGGER = getLogger(ImageDeleteEventHandler.class);

  private final ImageManager imageManager;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(LicenseImageDeleteEvent event) {
    LOGGER.info("[사업자 등록증 이미지 삭제] {}: {}", event.storeName(), event.licenseImage());

    imageManager.delete(event.licenseImage());
  }

}
