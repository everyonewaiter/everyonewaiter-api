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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "menu",
    indexes = {
        @Index(name = "idx_menu_store_id_position", columnList = "store_id, position asc"),
        @Index(name = "idx_menu_category_id_store_id", columnList = "category_id, store_id")
    }
)
@Getter
@ToString(exclude = {"store", "category", "menuOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Menu extends AggregateRootEntity<Menu> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "category_id",
      foreignKey = @ForeignKey(name = "fk_menu_category_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      nullable = false
  )
  private Category category;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "description", nullable = false, length = 100)
  private String description;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "spicy", nullable = false)
  private int spicy;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private MenuState state;

  @Enumerated(EnumType.STRING)
  @Column(name = "label", nullable = false)
  private MenuLabel label;

  @Column(name = "image", nullable = false, length = 30)
  private String image;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @Embedded
  private Position position;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("position asc, id asc")
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

    menu.registerEvent(new SseEvent(menu.getStore().getId(), MENU, CREATE));

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

    registerEvent(new SseEvent(store.getId(), MENU, UPDATE, getStringId()));
  }

  public void update(MenuUpdateRequest updateRequest, String image) {
    update(updateRequest);

    registerEvent(new MenuImageDeleteEvent(this.image));

    this.image = image;
  }

  public boolean movePosition(Menu other, MenuMovePositionRequest movePositionRequest) {
    boolean isMoved = this.position.move(other.position, movePositionRequest.where());

    if (isMoved) {
      registerEvent(new SseEvent(store.getId(), MENU, UPDATE));
    }

    return isMoved;
  }

  public boolean isVisible() {
    return this.state != MenuState.HIDE;
  }

  public boolean canOrder() {
    return this.state == MenuState.DEFAULT;
  }

  public void delete() {
    registerEvent(new MenuImageDeleteEvent(image));
    registerEvent(new SseEvent(store.getId(), MENU, DELETE, getStringId()));
  }

  public MenuOptionGroup getMenuOptionGroup(Long menuOptionGroupId) {
    return getMenuOptionGroups()
        .stream()
        .filter(
            menuOptionGroup -> requireNonNull(menuOptionGroup.getId()).equals(menuOptionGroupId)
        )
        .findFirst()
        .orElseThrow(MenuOptionGroupNotFoundException::new);
  }

  public List<MenuOptionGroup> getMenuOptionGroups() {
    return Collections.unmodifiableList(menuOptionGroups);
  }

}
