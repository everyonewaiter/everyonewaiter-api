package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "menu_option_group")
@Entity
@Getter
@ToString(exclude = "menuOptions", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionGroup extends Aggregate {

  public enum Type {MANDATORY, OPTIONAL}

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private Type type;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "menu_option_group_id", nullable = false, updatable = false)
  private List<MenuOption> menuOptions = new ArrayList<>();

  public static MenuOptionGroup create(
      String name,
      Type type,
      boolean printEnabled,
      List<MenuOption> menuOptions
  ) {
    MenuOptionGroup menuOptionGroup = new MenuOptionGroup();
    menuOptionGroup.name = name;
    menuOptionGroup.type = type;
    menuOptionGroup.printEnabled = printEnabled;
    menuOptionGroup.menuOptions.addAll(menuOptions);
    return menuOptionGroup;
  }

}
