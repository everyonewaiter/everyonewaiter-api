package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.domain.menu.event.MenuDeleteEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.CascadeType;
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
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "menu")
@Entity
@Getter
@ToString(exclude = {"category", "menuOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AggregateRoot<Menu> {

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

  @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @OrderBy("position asc, id asc")
  @OnDelete(action = OnDeleteAction.CASCADE)
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
    menu.position = new Position(lastPosition + 1);
    category.addMenu(menu);
    return menu;
  }

  public void addMenuOptionGroup(MenuOptionGroup menuOptionGroup) {
    this.menuOptionGroups.add(menuOptionGroup);
  }

  public boolean movePosition(Menu other, Position.Move where) {
    return this.position.move(other.position, where);
  }

  public void delete() {
    registerEvent(new MenuDeleteEvent(image));
  }

}
