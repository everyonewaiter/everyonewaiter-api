package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PositionMoveTest {

  @Test
  void moveNext() {
    int movedPosition = PositionMove.NEXT.move(1);

    assertThat(movedPosition).isEqualTo(2);
  }

  @Test
  void movePrev() {
    int movedPosition = PositionMove.PREV.move(1);

    assertThat(movedPosition).isEqualTo(1);
  }

}
