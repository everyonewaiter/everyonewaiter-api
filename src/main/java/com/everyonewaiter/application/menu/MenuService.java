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
    AtomicInteger menuOptionGroupPosition = new AtomicInteger(1);
    Menu menu = Menu.create(
        category.getId(),
        request.name(),
        request.description(),
        request.price(),
        request.spicy(),
        request.state(),
        request.label(),
        imageManager.upload(request.file(), "menu"),
        request.printEnabled(),
        lastPosition,
        request.menuOptionGroups()
            .stream()
            .map(menuOptionGroup -> {
                  AtomicInteger menuOptionPosition = new AtomicInteger(1);
                  return MenuOptionGroup.create(
                      menuOptionGroup.name(),
                      menuOptionGroup.type(),
                      menuOptionGroup.printEnabled(),
                      menuOptionGroupPosition.getAndIncrement(),
                      menuOptionGroup.menuOptions()
                          .stream()
                          .map(menuOption ->
                              MenuOption.create(
                                  menuOption.name(),
                                  menuOption.price(),
                                  menuOptionPosition.getAndIncrement()
                              )
                          )
                          .toList()
                  );
                }
            )
            .toList()
    );

    return menuRepository.save(menu).getId();
  }

}
