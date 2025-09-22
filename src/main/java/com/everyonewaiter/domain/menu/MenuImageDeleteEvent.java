package com.everyonewaiter.domain.menu;

import static java.util.Objects.requireNonNull;

import java.util.List;

public record MenuImageDeleteEvent(List<String> menuImages) {

  public MenuImageDeleteEvent(String menuImage) {
    this(List.of(requireNonNull(menuImage)));
  }

}
