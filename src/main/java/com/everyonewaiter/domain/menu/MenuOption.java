package com.everyonewaiter.domain.menu;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.shared.Position;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class MenuOption {

  private String name;

  private long price;

  private Position position;

  public static MenuOption create(MenuOptionModifyRequest request, int lastPosition) {
    MenuOption menuOption = new MenuOption();

    menuOption.name = requireNonNull(request.name());
    menuOption.price = request.price();
    menuOption.position = Position.next(lastPosition);

    return menuOption;
  }

  static List<MenuOption> createMenuOptions(List<MenuOptionModifyRequest> requests) {
    AtomicInteger menuOptionPosition = new AtomicInteger(0);

    return requests.stream()
        .map(request -> create(request, menuOptionPosition.getAndIncrement()))
        .toList();
  }

}
