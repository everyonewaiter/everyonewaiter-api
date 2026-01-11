package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PaginationTest {

  @Test
  fun `빠른 페이지 이동 계산을 위해 조회해야 할 데이터 수`() {
    val pagination1 = Pagination(1, 20, 5)
    val pagination2 = Pagination(5, 20, 10)

    assertThat(pagination1.countLimit()).isEqualTo(101)
    assertThat(pagination2.countLimit()).isEqualTo(281)
  }

  @Test
  fun `실제로 조회할 데이터 수`() {
    val pagination1 = Pagination(1, 10)
    val pagination2 = Pagination(1, 20)

    assertThat(pagination1.limit()).isEqualTo(pagination1.size)
    assertThat(pagination2.limit()).isEqualTo(pagination2.size)
  }

  @Test
  fun `데이터 조회 시 건너뛸 행 수`() {
    val pagination1 = Pagination(1, 10)
    val pagination2 = Pagination(5, 20)
    val pagination3 = Pagination(10, 100)

    assertThat(pagination1.offset()).isEqualTo(0)
    assertThat(pagination2.offset()).isEqualTo(80)
    assertThat(pagination3.offset()).isEqualTo(900)
  }
}
