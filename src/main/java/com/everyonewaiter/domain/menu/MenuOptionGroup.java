package com.everyonewaiter.domain.menu;

import static com.everyonewaiter.domain.menu.MenuOption.createMenuOptions;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateEntity;
import com.everyonewaiter.domain.shared.Position;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "menu_option_group")
@Getter
@ToString(exclude = {"menu", "menuOptions"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class MenuOptionGroup extends AggregateEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "menu_id",
      foreignKey = @ForeignKey(name = "fk_menu_option_group_menu_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      nullable = false,
      updatable = false
  )
  private Menu menu;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private MenuOptionGroupType type;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @Embedded
  private Position position;

  @ElementCollection
  @CollectionTable(
      name = "menu_option",
      foreignKey = @ForeignKey(name = "fk_menu_option_menu_option_group_id", foreignKeyDefinition = "ON DELETE CASCADE"),
      joinColumns = @JoinColumn(name = "menu_option_group_id", nullable = false)
  )
  @OrderBy("position asc")
  private List<MenuOption> menuOptions = new ArrayList<>();

  public static MenuOptionGroup create(
      Menu menu,
      MenuOptionGroupModifyRequest request,
      int lastPosition
  ) {
    MenuOptionGroup menuOptionGroup = new MenuOptionGroup();

    menuOptionGroup.menu = requireNonNull(menu);
    menuOptionGroup.name = requireNonNull(request.name());
    menuOptionGroup.type = requireNonNull(request.type());
    menuOptionGroup.printEnabled = request.printEnabled();
    menuOptionGroup.position = Position.next(lastPosition);
    menuOptionGroup.menuOptions.addAll(createMenuOptions(request.menuOptions()));

    validateDuplicateMenuOptionName(menuOptionGroup);

    return menuOptionGroup;
  }

  public static List<MenuOptionGroup> createMenuOptionGroups(
      Menu menu,
      List<MenuOptionGroupModifyRequest> requests
  ) {
    AtomicInteger menuOptionGroupPosition = new AtomicInteger(0);

    return requests.stream()
        .map(request -> create(menu, request, menuOptionGroupPosition.getAndIncrement()))
        .toList();
  }

  private static void validateDuplicateMenuOptionName(MenuOptionGroup menuOptionGroup) {
    List<MenuOption> menuOptions = menuOptionGroup.getMenuOptions();

    Set<String> distinctOptions = menuOptions.stream()
        .map(MenuOption::getName)
        .collect(Collectors.toSet());

    if (distinctOptions.size() != menuOptions.size()) {
      throw new DuplicateMenuOptionNameException();
    }
  }

  public boolean isMandatory() {
    return this.type == MenuOptionGroupType.MANDATORY;
  }

  public long getDiscountPrice() {
    if (isMandatory()) {
      return getMenuOptions().stream()
          .mapToLong(MenuOption::getPrice)
          .min()
          .orElse(0L);
    } else {
      return getMenuOptions().stream()
          .filter(menuOption -> menuOption.getPrice() < 0)
          .mapToLong(MenuOption::getPrice)
          .sum();
    }
  }

  public MenuOption getMenuOption(String name, long price) {
    return menuOptions.stream()
        .filter(menuOption ->
            menuOption.getName().equals(name) && menuOption.getPrice() == price
        )
        .findFirst()
        .orElseThrow(MenuOptionNotFoundException::new);
  }

  public List<MenuOption> getMenuOptions() {
    return Collections.unmodifiableList(menuOptions);
  }

}
