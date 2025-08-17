package com.everyonewaiter.application.image;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.domain.menu.CategoryDeleteEvent;
import com.everyonewaiter.domain.menu.MenuImageDeleteEvent;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.store.LicenseImageDeleteEvent;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(CategoryDeleteEvent event) {
    LOGGER.info("[카테고리 메뉴 이미지 삭제] 카테고리 ID: {}", event.categoryId());

    List<String> menuImages = event.menus().stream()
        .map(Menu::getImage)
        .toList();

    if (menuImages.isEmpty()) {
      return;
    }

    try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      executorService.invokeAll(
          menuImages.stream()
              .map(menuImage ->
                  (Callable<String>) () -> {
                    imageManager.delete(menuImage);

                    return menuImage;
                  }
              )
              .toList()
      );
    } catch (InterruptedException exception) {
      LOGGER.error("[카테고리 메뉴 이미지 삭제 중단] 카테고리 ID: {} 메뉴 이미지: {}",
          event.categoryId(), menuImages, exception);

      Thread.currentThread().interrupt();
    }
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void handle(MenuImageDeleteEvent event) {
    LOGGER.info("[메뉴 이미지 삭제] 이미지: {}", event.menuImage());

    imageManager.delete(event.menuImage());
  }

}
