package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.menu.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "MenuSimpleResponses")
public record MenuSimpleResponses(List<MenuSimpleResponse> menus) {

  public static MenuSimpleResponses from(List<Menu> menus) {
    return new MenuSimpleResponses(
        menus.stream()
            .map(MenuSimpleResponse::from)
            .toList()
    );
  }

}
