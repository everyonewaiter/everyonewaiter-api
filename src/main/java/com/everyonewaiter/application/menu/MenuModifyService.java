package com.everyonewaiter.application.menu;

import static com.everyonewaiter.domain.sse.ServerAction.DELETE;
import static com.everyonewaiter.domain.sse.SseCategory.MENU;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.application.menu.provided.CategoryFinder;
import com.everyonewaiter.application.menu.provided.MenuFinder;
import com.everyonewaiter.application.menu.provided.MenuManager;
import com.everyonewaiter.application.menu.required.MenuRepository;
import com.everyonewaiter.application.support.CacheName;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.ExceedMaxMenuCountException;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuCreateRequest;
import com.everyonewaiter.domain.menu.MenuDeleteRequest;
import com.everyonewaiter.domain.menu.MenuImageDeleteEvent;
import com.everyonewaiter.domain.menu.MenuMovePositionRequest;
import com.everyonewaiter.domain.menu.MenuNotFoundException;
import com.everyonewaiter.domain.menu.MenuUpdateRequest;
import com.everyonewaiter.domain.sse.SseEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class MenuModifyService implements MenuManager {

  private final ImageManager imageManager;
  private final CategoryFinder categoryFinder;
  private final MenuFinder menuFinder;
  private final MenuRepository menuRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Menu create(
      Long categoryId,
      Long storeId,
      MenuCreateRequest createRequest,
      MultipartFile file
  ) {
    validateMenuCreate(categoryId);

    Category category = categoryFinder.findOrThrow(categoryId, storeId);
    int lastPosition = menuRepository.findLastPosition(categoryId);

    Menu menu = Menu.create(
        category,
        createRequest,
        imageManager.upload("menu", file),
        lastPosition
    );

    return menuRepository.save(menu);
  }

  private void validateMenuCreate(Long categoryId) {
    Long menuCount = menuRepository.count(categoryId);

    if (menuCount >= 50) {
      throw new ExceedMaxMenuCountException();
    }
  }

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Menu update(Long menuId, Long storeId, MenuUpdateRequest updateRequest) {
    Menu menu = menuFinder.findOrThrow(menuId, storeId);

    menu.update(updateRequest);

    return menuRepository.save(menu);
  }

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Menu update(
      Long menuId,
      Long storeId,
      MenuUpdateRequest updateRequest,
      MultipartFile file
  ) {
    Menu menu = menuFinder.findOrThrow(menuId, storeId);

    menu.update(updateRequest, imageManager.upload("menu", file));

    return menuRepository.save(menu);
  }

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Menu movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      MenuMovePositionRequest movePositionRequest
  ) {
    Menu source = menuFinder.findOrThrow(sourceId, storeId);
    Menu target = menuFinder.findOrThrow(targetId, storeId);

    boolean isMoved = source.movePosition(target, movePositionRequest);
    if (isMoved) {
      menuRepository.shiftPosition(source);
    }

    return menuRepository.save(source);
  }

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void delete(Long menuId, Long storeId, Long categoryId) {
    Menu menu = menuFinder.findOrThrow(menuId, storeId, categoryId);

    menu.delete();

    menuRepository.delete(menu);
  }

  @Override
  @DistributedLock(key = "#storeId + '-menu'")
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void deleteAll(Long storeId, MenuDeleteRequest deleteRequest) {
    List<Menu> menus = menuFinder.findAll(storeId, deleteRequest.menuIds());

    if (menus.size() == deleteRequest.menuIds().size()) {
      menuRepository.deleteAll(menus);

      var event1 = new MenuImageDeleteEvent(menus.stream().map(Menu::getImage).toList());
      var event2 = new SseEvent(storeId, MENU, DELETE, deleteRequest.getStringMenuIds());

      applicationEventPublisher.publishEvent(event1);
      applicationEventPublisher.publishEvent(event2);
    } else {
      throw new MenuNotFoundException();
    }
  }

}
