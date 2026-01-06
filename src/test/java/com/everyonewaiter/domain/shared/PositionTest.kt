package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {

  @Test
  fun `n 다음 포지션 생성`() {
    val n = 10

    val position = Position.next(n)

    assertThat(position.value).isEqualTo(n + 1)
  }

  @Test
  fun `포지션 복사`() {
    val position = createPosition(10)

    val copied = Position.copy(position)

    assertThat(copied).isEqualTo(position)
  }

  @Test
  fun `포지션 이동(NEXT)`() {
    val position1 = createPosition(10)
    val position2 = createPosition(20)

    val isMoved = position1.move(position2, PositionMove.NEXT)

    assertThat(isMoved).isTrue
    assertThat(position1.value).isEqualTo(position2.value + 1)
  }

  @Test
  fun `포지션 이동(PREV)`() {
    val position1 = createPosition(10)
    val position2 = createPosition(20)

    val isMoved = position1.move(position2, PositionMove.PREV)

    assertThat(isMoved).isTrue
    assertThat(position1.value).isEqualTo(position2.value)
  }

  @Test
  fun `이동한 포지션이 기존과 같은 경우 이동 여부 false`() {
    val position1 = createPosition(10)
    val position2 = createPosition(9)

    val isMoved1 = position1.move(position2, PositionMove.NEXT)

    assertThat(isMoved1).isFalse
    assertThat(position1.value).isEqualTo(10)

    val position3 = createPosition(10)
    val position4 = createPosition(10)

    val isMoved2 = position3.move(position4, PositionMove.PREV)

    assertThat(isMoved2).isFalse
    assertThat(position3.value).isEqualTo(10)
  }
}
