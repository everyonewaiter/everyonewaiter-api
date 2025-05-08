package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "menu_option")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption extends Aggregate {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  public static MenuOption create(String name, long price) {
    MenuOption menuOption = new MenuOption();
    menuOption.name = name;
    menuOption.price = price;
    return menuOption;
  }

}
