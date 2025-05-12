package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Embedded
  private Position position;

  public static MenuOption create(String name, long price, int position) {
    MenuOption menuOption = new MenuOption();
    menuOption.name = name;
    menuOption.price = price;
    menuOption.position = new Position(position);
    return menuOption;
  }

}
