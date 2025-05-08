package com.everyonewaiter.application.menu.request;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
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
      List<CreateOptionGroup> menuOptionGroups,
      MultipartFile file
  ) {

  }

  public record CreateOptionGroup(
      String name,
      MenuOptionGroup.Type type,
      boolean printEnabled,
      List<CreateOption> menuOptions
  ) {

  }

  public record CreateOption(String name, long price) {

  }

}
