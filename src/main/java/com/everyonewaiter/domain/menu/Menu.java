package com.everyonewaiter.domain.menu;

import static com.everyonewaiter.domain.menu.MenuOptionGroup.createMenuOptionGroups;
import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.DELETE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.MENU;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.Position;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"store", "category", "menuOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Menu extends AggregateRootEntity<Menu> {

  private Store store;

  private Category category;

  private String name;

  private String description;

  private long price;

  private int spicy;

  private MenuState state;

  private MenuLabel label;

  private String image;

  private boolean printEnabled;

  private Position position;

  private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

  public static Menu create(
      Category category,
      MenuCreateRequest createRequest,
      String image,
      int lastPosition
  ) {
    Menu menu = new Menu();

    menu.store = requireNonNull(category.getStore());
    menu.category = requireNonNull(category);
    menu.name = requireNonNull(createRequest.name());
    menu.description = requireNonNull(createRequest.description());
    menu.price = createRequest.price();
    menu.spicy = createRequest.spicy();
    menu.state = requireNonNull(createRequest.state());
    menu.label = requireNonNull(createRequest.label());
    menu.image = requireNonNull(image);
    menu.printEnabled = createRequest.printEnabled();
    menu.position = Position.next(lastPosition);
    menu.menuOptionGroups.addAll(createMenuOptionGroups(menu, createRequest.menuOptionGroups()));

    validateMenuOptionPrice(menu);

    menu.registerEvent(new SseEvent(menu.getStore().getNonNullId(), MENU, CREATE));

    return menu;
  }

  private static void validateMenuOptionPrice(Menu menu) {
    long menuPrice = menu.getPrice();

    for (MenuOptionGroup menuOptionGroup : menu.getMenuOptionGroups()) {
      menuPrice -= menuOptionGroup.getDiscountPrice();
    }

    if (menuPrice < 0) {
      throw new InvalidMenuOptionDiscountPriceException();
    }
  }

  public void update(MenuUpdateRequest updateRequest) {
    this.name = requireNonNull(updateRequest.name());
    this.description = requireNonNull(updateRequest.description());
    this.price = updateRequest.price();
    this.spicy = updateRequest.spicy();
    this.state = requireNonNull(updateRequest.state());
    this.label = requireNonNull(updateRequest.label());
    this.printEnabled = updateRequest.printEnabled();

    this.menuOptionGroups.clear();
    this.menuOptionGroups.addAll(createMenuOptionGroups(this, updateRequest.menuOptionGroups()));

    validateMenuOptionPrice(this);

    registerEvent(new SseEvent(store.getNonNullId(), MENU, UPDATE, getNonNullId()));
  }

  public void update(MenuUpdateRequest updateRequest, String image) {
    update(updateRequest);

    registerEvent(new MenuImageDeleteEvent(this.image));

    this.image = image;
  }

  public boolean movePosition(Menu other, MenuMovePositionRequest movePositionRequest) {
    boolean isMoved = this.position.move(other.position, movePositionRequest.where());

    if (isMoved) {
      registerEvent(new SseEvent(store.getNonNullId(), MENU, UPDATE));
    }

    return isMoved;
  }

  public boolean canOrder() {
    return this.state == MenuState.DEFAULT;
  }

  public void delete() {
    registerEvent(new MenuImageDeleteEvent(image));
    registerEvent(new SseEvent(store.getNonNullId(), MENU, DELETE, getNonNullId()));
  }

  public MenuOptionGroup getMenuOptionGroup(Long menuOptionGroupId) {
    return getMenuOptionGroups()
        .stream()
        .filter(menuOptionGroup -> menuOptionGroup.getNonNullId().equals(menuOptionGroupId))
        .findFirst()
        .orElseThrow(MenuOptionGroupNotFoundException::new);
  }

  public List<MenuOptionGroup> getMenuOptionGroups() {
    return Collections.unmodifiableList(menuOptionGroups);
  }

}
