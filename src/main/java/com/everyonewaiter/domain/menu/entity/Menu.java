package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "menu")
@Entity
@Getter
@ToString(exclude = {"menuOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends AggregateRoot<Menu> {

  public enum State {DEFAULT, HIDE, SOLD_OUT}

  public enum Label {DEFAULT, NEW, BEST, RECOMMEND}

  @Column(name = "category_id", nullable = false, updatable = false)
  private Long categoryId;

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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "menu_id", nullable = false, updatable = false)
  @OrderBy("position asc, id asc")
  private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

  public static Menu create(
      Long categoryId,
      String name,
      String description,
      long price,
      int spicy,
      State state,
      Label label,
      String image,
      boolean printEnabled,
      int lastPosition,
      List<MenuOptionGroup> menuOptionGroups
  ) {
    Menu menu = new Menu();
    menu.categoryId = categoryId;
    menu.name = name;
    menu.description = description;
    menu.price = price;
    menu.spicy = spicy;
    menu.state = state;
    menu.label = label;
    menu.image = image;
    menu.printEnabled = printEnabled;
    menu.position = new Position(lastPosition + 1);
    menu.menuOptionGroups.addAll(menuOptionGroups);
    return menu;
  }

}
