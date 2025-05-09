package com.everyonewaiter.application.menu;

import com.everyonewaiter.domain.image.service.ImageManager;
import com.everyonewaiter.domain.menu.event.MenuDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class MenuDeleteEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuDeleteEventHandler.class);

  private final ImageManager imageManager;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(MenuDeleteEvent event) {
    LOGGER.info("[메뉴 삭제] 이미지: {}", event.menuImage());
    imageManager.delete(event.menuImage());
  }

}
