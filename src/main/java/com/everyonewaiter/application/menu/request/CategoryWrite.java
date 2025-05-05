package com.everyonewaiter.application.menu.request;

import com.everyonewaiter.global.domain.entity.Position;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryWrite {

  public record Create(String name) {

  }

  public record Update(String name) {

  }

  public record MovePosition(Position.Move where) {

  }

}
