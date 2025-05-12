package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "menu_option_group")
@Entity
@Getter
@ToString(exclude = {"menu", "menuOptions"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionGroup extends Aggregate {

  public enum Type {MANDATORY, OPTIONAL}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Menu menu;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private Type type;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @Embedded
  private Position position;

  @ElementCollection
  @CollectionTable(name = "menu_option", joinColumns = @JoinColumn(name = "menu_option_group_id"))
  @OrderBy("position asc")
  private List<MenuOption> menuOptions = new ArrayList<>();

  public static MenuOptionGroup create(
      Menu menu,
      String name,
      Type type,
      boolean printEnabled,
      int position
  ) {
    MenuOptionGroup menuOptionGroup = new MenuOptionGroup();
    menuOptionGroup.menu = menu;
    menuOptionGroup.name = name;
    menuOptionGroup.type = type;
    menuOptionGroup.printEnabled = printEnabled;
    menuOptionGroup.position = new Position(position);
    return menuOptionGroup;
  }

  public void addMenuOption(MenuOption menuOption) {
    this.menuOptions.add(menuOption);
  }

  public List<MenuOption> getMenuOptions() {
    return Collections.unmodifiableList(menuOptions);
  }

}
