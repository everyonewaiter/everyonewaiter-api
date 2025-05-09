package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "menu_option")
@Entity
@Getter
@ToString(exclude = "menuOptionGroup", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption extends Aggregate {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_option_group_id", nullable = false)
  private MenuOptionGroup menuOptionGroup;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Embedded
  private Position position;

  public static MenuOption create(
      MenuOptionGroup menuOptionGroup,
      String name,
      long price,
      int position
  ) {
    MenuOption menuOption = new MenuOption();
    menuOption.menuOptionGroup = menuOptionGroup;
    menuOption.name = name;
    menuOption.price = price;
    menuOption.position = new Position(position);
    menuOptionGroup.addMenuOption(menuOption);
    return menuOption;
  }

}
