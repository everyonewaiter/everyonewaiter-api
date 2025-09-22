package com.everyonewaiter.domain.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionMove {

  PREV(0),
  NEXT(1),
  ;

  private final int moveCount;

  int move(int position) {
    return position + moveCount;
  }

}
