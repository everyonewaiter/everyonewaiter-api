package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.application.menu.request.MenuWrite;
import com.everyonewaiter.application.menu.response.MenuResponse;
import com.everyonewaiter.application.support.CacheName;
import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOption;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.domain.menu.service.MenuValidator;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final ImageManager imageManager;
  private final CategoryRepository categoryRepository;
  private final MenuValidator menuValidator;
  private final MenuRepository menuRepository;

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public Long create(Long categoryId, Long storeId, MenuWrite.Create request) {
    Category category = categoryRepository.findByIdAndStoreIdOrThrow(categoryId, storeId);
    menuValidator.validateExceedMaxCount(categoryId);

    int lastPosition = menuRepository.findLastPositionByCategoryId(categoryId);
    Menu menu = Menu.create(
        category,
        request.name(),
        request.description(),
        request.price(),
        request.spicy(),
        request.state(),
        request.label(),
        imageManager.upload("menu", request.file()),
        request.printEnabled(),
        lastPosition
    );

    return menuRepository.save(menu).getId();
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void update(Long menuId, Long storeId, MenuWrite.Update request) {
    Menu menu = menuRepository.findByIdAndStoreIdOrThrow(menuId, storeId);
    menu.update(
        request.name(),
        request.description(),
        request.price(),
        request.spicy(),
        request.state(),
        request.label(),
        request.printEnabled()
    );
    menuRepository.save(menu);
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void update(
      Long menuId,
      Long storeId,
      MenuWrite.Update request,
      MultipartFile file
  ) {
    Menu menu = menuRepository.findByIdAndStoreIdOrThrow(menuId, storeId);
    menu.update(
        request.name(),
        request.description(),
        request.price(),
        request.spicy(),
        request.state(),
        request.label(),
        request.printEnabled()
    );
    menu.updateMenuImage(imageManager.upload("menu", file));
    menuRepository.save(menu);
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void replaceMenuOptionGroups(
      Long menuId,
      Long storeId,
      List<MenuWrite.OptionGroup> request
  ) {
    Menu menu = menuRepository.findByIdAndStoreIdOrThrow(menuId, storeId);
    List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

    AtomicInteger menuOptionGroupPosition = new AtomicInteger(0);
    for (MenuWrite.OptionGroup optionGroup : request) {
      MenuOptionGroup menuOptionGroup = MenuOptionGroup.create(
          menu,
          optionGroup.name(),
          optionGroup.type(),
          optionGroup.printEnabled(),
          menuOptionGroupPosition.getAndIncrement()
      );

      AtomicInteger menuOptionPosition = new AtomicInteger(0);
      for (MenuWrite.Option option : optionGroup.menuOptions()) {
        MenuOption menuOption = MenuOption.create(
            option.name(),
            option.price(),
            menuOptionPosition.getAndIncrement()
        );
        menuOptionGroup.addMenuOption(menuOption);
      }

      menuValidator.validateOptionPrice(menu.getPrice(), menuOptionGroup);
      menuOptionGroups.add(menuOptionGroup);
    }

    menuRepository.saveAllMenuOptionGroups(menu.getId(), menuOptionGroups);
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      MenuWrite.MovePosition request
  ) {
    Menu source = menuRepository.findByIdAndStoreIdOrThrow(sourceId, storeId);
    Menu target = menuRepository.findByIdAndStoreIdOrThrow(targetId, storeId);

    boolean isMoved = source.movePosition(target, request.where());
    if (isMoved) {
      menuRepository.shiftPosition(source);
      menuRepository.save(source);
    }
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void delete(Long menuId, Long storeId, Long categoryId) {
    Menu menu = menuRepository.findByIdAndStoreIdAndCategoryIdOrThrow(menuId, storeId, categoryId);
    menu.delete();
    menuRepository.delete(menu);
  }

  @Transactional
  @CacheEvict(cacheNames = CacheName.STORE_MENU, key = "#storeId")
  public void deleteAll(Long storeId, MenuWrite.Delete request) {
    List<Menu> menus = menuRepository.findAllByStoreIdAndIds(storeId, request.menuIds());
    if (menus.size() == request.menuIds().size()) {
      menus.forEach(Menu::delete);
      menuRepository.deleteAll(menus);
    } else {
      throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
    }
  }

  public MenuResponse.Simples readAllSimples(Long storeId, Long categoryId) {
    List<Menu> menus = menuRepository.findAllByStoreIdAndCategoryId(storeId, categoryId);
    return MenuResponse.Simples.from(menus);
  }

  @Transactional(readOnly = true)
  public MenuResponse.Detail readDetail(Long menuId, Long storeId, Long categoryId) {
    Menu menu = menuRepository.findByIdAndStoreIdAndCategoryIdOrThrow(menuId, storeId, categoryId);
    return MenuResponse.Detail.from(menu);
  }

}
