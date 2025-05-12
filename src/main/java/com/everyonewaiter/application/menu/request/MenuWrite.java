package com.everyonewaiter.application.menu.request;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.global.domain.entity.Position;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuWrite {

  public record Create(
      String name,
      String description,
      long price,
      int spicy,
      Menu.State state,
      Menu.Label label,
      boolean printEnabled,
      List<OptionGroup> menuOptionGroups,
      MultipartFile file
  ) {

  }

  public record Update(
      String name,
      String description,
      long price,
      int spicy,
      Menu.State state,
      Menu.Label label,
      boolean printEnabled,
      List<OptionGroup> menuOptionGroups
  ) {

  }

  public record OptionGroup(
      String name,
      MenuOptionGroup.Type type,
      boolean printEnabled,
      List<Option> menuOptions
  ) {

  }

  public record Option(String name, long price) {

  }

  public record MovePosition(Position.Move where) {

  }

}
