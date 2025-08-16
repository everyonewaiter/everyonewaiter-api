package com.everyonewaiter.application.menu;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.event.CategoryDeleteEvent;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class CategoryDeleteEventHandler {

  private static final Logger LOGGER = getLogger(CategoryDeleteEventHandler.class);

  private final ImageManager imageManager;
  private final MenuRepository menuRepository;

  @Async("eventTaskExecutor")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  public void consume(CategoryDeleteEvent event) {
    LOGGER.info("[카테고리 메뉴 삭제] 카테고리 ID: {}", event.categoryId());
    List<Menu> menus = menuRepository.findAllByCategoryId(event.categoryId());

    try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      executorService.invokeAll(
          menus.stream()
              .map(menu ->
                  (Callable<String>) () -> {
                    imageManager.delete(menu.getImage());
                    return menu.getImage();
                  }
              )
              .toList()
      );
    } catch (InterruptedException exception) {
      LOGGER.error("[카테고리 메뉴 이미지 삭제 중단] 카테고리 ID: {}", event.categoryId(), exception);
      Thread.currentThread().interrupt();
    }

    menuRepository.deleteAllByCategoryId(event.categoryId());
  }

}
