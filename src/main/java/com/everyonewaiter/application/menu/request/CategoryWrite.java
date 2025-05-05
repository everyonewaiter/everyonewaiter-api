package com.everyonewaiter.application.menu.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryWrite {

  public record Create(String name) {

  }

}
