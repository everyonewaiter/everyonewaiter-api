package com.everyonewaiter.global.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {

  @Getter
  @RequiredArgsConstructor
  public enum Move {
    PREV(0),
    NEXT(1),
    ;

    private final int moveCount;
  }

  @Column(name = "position", nullable = false)
  private int value;

  public Position(int value) {
    this.value = value;
  }

  public boolean move(Position other, Move move) {
    int movedPosition = other.value + move.getMoveCount();
    if (this.value == movedPosition) {
      return false;
    } else {
      this.value = movedPosition;
      return true;
    }
  }

}
