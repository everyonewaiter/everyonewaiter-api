package com.everyonewaiter.application.menu;

import com.everyonewaiter.application.menu.request.MenuWrite;
import com.everyonewaiter.domain.image.service.ImageManager;
import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOption;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.domain.menu.service.MenuValidator;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final ImageManager imageManager;
  private final CategoryRepository categoryRepository;
  private final MenuValidator menuValidator;
  private final MenuRepository menuRepository;

  @Transactional
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
        imageManager.upload(request.file(), "menu"),
        request.printEnabled(),
        lastPosition
    );

    AtomicInteger menuOptionGroupPosition = new AtomicInteger(1);
    for (MenuWrite.CreateOptionGroup createOptionGroup : request.menuOptionGroups()) {
      MenuOptionGroup menuOptionGroup = MenuOptionGroup.create(
          menu,
          createOptionGroup.name(),
          createOptionGroup.type(),
          createOptionGroup.printEnabled(),
          menuOptionGroupPosition.getAndIncrement()
      );

      AtomicInteger menuOptionPosition = new AtomicInteger(1);
      for (MenuWrite.CreateOption createOption : createOptionGroup.menuOptions()) {
        MenuOption.create(
            menuOptionGroup,
            createOption.name(),
            createOption.price(),
            menuOptionPosition.getAndIncrement()
        );
      }
    }

    return menuRepository.save(menu).getId();
  }

  @Transactional
  public void delete(Long menuId, Long categoryId, Long storeId) {
    Menu menu = menuRepository.findByIdAndCategoryIdAndStoreIdOrThrow(menuId, categoryId, storeId);
    menu.delete();
    menuRepository.delete(menu);
  }

}
