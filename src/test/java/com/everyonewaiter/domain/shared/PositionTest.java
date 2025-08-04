package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PositionTest {

  @Test
  void next() {
    Position nextPosition = Position.next(0);

    assertThat(nextPosition.getValue()).isEqualTo(1);
  }

  @Test
  void copy() {
    Position position = new Position(); // 0

    Position copiedPosition = Position.copy(position);

    assertThat(copiedPosition).isEqualTo(position);
  }

  @Test
  void moveNext() {
    Position position1 = new Position(); // 0
    Position position2 = Position.next(5);// 6

    boolean isMoved = position1.move(position2, PositionMove.NEXT);

    assertThat(isMoved).isTrue();
    assertThat(position1.getValue()).isEqualTo(7);
  }

  @Test
  void movePrev() {
    Position position1 = new Position(); // 0
    Position position2 = Position.next(5);// 6

    boolean isMoved = position1.move(position2, PositionMove.PREV);

    assertThat(isMoved).isTrue();
    assertThat(position1.getValue()).isEqualTo(6);
  }

  @Test
  void notMove() {
    Position position1 = new Position(); // 0
    Position position2 = new Position(); // 0

    boolean isMoved = position1.move(position2, PositionMove.PREV);

    assertThat(isMoved).isFalse();
    assertThat(position1.getValue()).isZero();
  }

}
