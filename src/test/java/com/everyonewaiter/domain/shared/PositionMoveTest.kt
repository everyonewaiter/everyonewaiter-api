package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionMoveTest {

  @Test
  fun `포지션을 n 다음으로 이동`() {
    val n = 10

    val movedPosition = PositionMove.NEXT.move(n)

    assertThat(movedPosition).isEqualTo(n + 1)
  }

  @Test
  fun `포지션을 n 으로 이동`() {
    val n = 10

    val movedPosition = PositionMove.PREV.move(n)

    assertThat(movedPosition).isEqualTo(n)
  }
}
