package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.menu.event.MenuImageDeleteEvent;
import com.everyonewaiter.domain.shared.Position;
import com.everyonewaiter.domain.shared.PositionMove;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "menu")
@Entity
@Getter
@ToString(exclude = {"category", "menuOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AggregateRootEntity<Menu> {

  public enum State {DEFAULT, HIDE, SOLD_OUT}

  public enum Label {DEFAULT, NEW, BEST, RECOMMEND}

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Category category;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "spicy", nullable = false)
  private int spicy;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state;

  @Enumerated(EnumType.STRING)
  @Column(name = "label", nullable = false)
  private Label label;

  @Column(name = "image", nullable = false)
  private String image;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @Embedded
  private Position position;

  @OneToMany(mappedBy = "menu")
  @OrderBy("position asc, id asc")
  private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

  public static Menu create(
      Category category,
      String name,
      String description,
      long price,
      int spicy,
      State state,
      Label label,
      String image,
      boolean printEnabled,
      int lastPosition
  ) {
    Menu menu = new Menu();
    menu.storeId = category.getStoreId();
    menu.category = category;
    menu.name = name;
    menu.description = description;
    menu.price = price;
    menu.spicy = spicy;
    menu.state = state;
    menu.label = label;
    menu.image = image;
    menu.printEnabled = printEnabled;
    menu.position = Position.next(lastPosition);
    return menu;
  }

  public void update(
      String name,
      String description,
      long price,
      int spicy,
      State state,
      Label label,
      boolean printEnabled
  ) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.spicy = spicy;
    this.state = state;
    this.label = label;
    this.printEnabled = printEnabled;
  }

  public void updateMenuImage(String image) {
    registerEvent(new MenuImageDeleteEvent(this.image));
    this.image = image;
  }

  public boolean movePosition(Menu other, PositionMove where) {
    return this.position.move(other.position, where);
  }

  public void delete() {
    registerEvent(new MenuImageDeleteEvent(image));
  }

  public boolean canOrder() {
    return state == State.DEFAULT;
  }

  public MenuOptionGroup getMenuOptionGroup(Long menuOptionGroupId) {
    return getMenuOptionGroups()
        .stream()
        .filter(menuOptionGroup ->
            Objects.requireNonNull(menuOptionGroup.getId()).equals(menuOptionGroupId)
        )
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND));
  }

  public List<MenuOptionGroup> getMenuOptionGroups() {
    return Collections.unmodifiableList(menuOptionGroups);
  }

}
