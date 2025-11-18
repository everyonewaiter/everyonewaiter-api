package com.everyonewaiter.domain.shared;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Position {

  @Column(name = "position", nullable = false)
  private int value;

  public static Position next(int lastPosition) {
    Position position = new Position();

    position.value = lastPosition + 1;

    return position;
  }

  public static Position copy(Position position) {
    Position copy = new Position();

    copy.value = position.value;

    return copy;
  }

  private static boolean isMoved(int prevPositionValue, int movedPositionValue) {
    return prevPositionValue != movedPositionValue;
  }

  public boolean move(Position other, PositionMove where) {
    int prevPositionValue = this.value;
    int movedPositionValue = where.move(other.value);

    this.value = movedPositionValue;

    return isMoved(prevPositionValue, movedPositionValue);
  }

}
