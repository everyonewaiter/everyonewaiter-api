package com.everyonewaiter.application.menu;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.domain.menu.event.MenuImageDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class MenuImageDeleteEventHandler {

  private static final Logger LOGGER = getLogger(MenuImageDeleteEventHandler.class);

  private final ImageManager imageManager;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(MenuImageDeleteEvent event) {
    LOGGER.info("[메뉴 이미지 삭제] 이미지: {}", event.menuImage());

    imageManager.delete(event.menuImage());
  }

}
