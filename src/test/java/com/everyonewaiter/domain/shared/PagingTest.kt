package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PagingTest {

  @Test
  fun `이전, 다음 페이지 여부`() {
    // 현재 페이지 1, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination1 = Pagination(1, 3)

    val paging1 = createPaging(pagination1)

    assertThat(paging1.hasPrevious()).isFalse
    assertThat(paging1.hasNext()).isTrue

    // 현재 페이지 1, 페이지에 보여줄 컨텐츠 사이즈 10
    val pagination2 = Pagination(1, 10)

    val paging2 = createPaging(pagination2)

    assertThat(paging2.hasPrevious()).isFalse
    assertThat(paging2.hasNext()).isFalse

    // 현재 페이지 2, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination3 = Pagination(2, 3)

    val paging3 = createPaging(pagination3)

    assertThat(paging3.hasPrevious()).isTrue
    assertThat(paging3.hasNext()).isTrue

    // 현재 페이지 4, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination4 = Pagination(4, 3)

    val paging4 = createPaging(pagination4)

    assertThat(paging4.hasPrevious()).isTrue
    assertThat(paging4.hasNext()).isFalse
  }

  @Test
  fun `첫번째, 마지막 페이지 여부`() {
    // 현재 페이지 1, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination1 = Pagination(1, 3)

    val paging1 = createPaging(pagination1)

    assertThat(paging1.isFirst).isTrue
    assertThat(paging1.isLast).isFalse

    // 현재 페이지 1, 페이지에 보여줄 컨텐츠 사이즈 10
    val pagination2 = Pagination(1, 10)

    val paging2 = createPaging(pagination2)

    assertThat(paging2.isFirst).isTrue
    assertThat(paging2.isLast).isTrue

    // 현재 페이지 2, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination3 = Pagination(2, 3)

    val paging3 = createPaging(pagination3)

    assertThat(paging3.isFirst).isFalse
    assertThat(paging3.isLast).isFalse

    // 현재 페이지 4, 페이지에 보여줄 컨텐츠 사이즈 3
    val pagination4 = Pagination(4, 3)

    val paging4 = createPaging(pagination4)

    assertThat(paging4.isFirst).isFalse
    assertThat(paging4.isLast).isTrue
  }

  @Test
  fun `이전, 다음 빠른 이동 페이지 번호`() {
    // 현재 페이지 1, 페이지에 보여줄 컨텐츠 사이즈 1, 빠른 이동 시 이동할 페이지 단위 3
    val pagination1 = Pagination(1, 1, 3)

    val paging1 = createPaging(pagination1)

    assertThat(paging1.fastBackwardPage).isEqualTo(1)
    assertThat(paging1.fastForwardPage).isEqualTo(4)

    // 현재 페이지 4, 페이지에 보여줄 컨텐츠 사이즈 1, 빠른 이동 시 이동할 페이지 단위 3
    val pagination2 = Pagination(4, 1, 3)

    val paging2 = createPaging(pagination2)

    assertThat(paging2.fastBackwardPage).isEqualTo(1)
    assertThat(paging2.fastForwardPage).isEqualTo(7)

    // 현재 페이지 5, 페이지에 보여줄 컨텐츠 사이즈 1, 빠른 이동 시 이동할 페이지 단위 3
    val pagination3 = Pagination(5, 1, 3)

    val paging3 = createPaging(pagination3)

    assertThat(paging3.fastBackwardPage).isEqualTo(2)
    assertThat(paging3.fastForwardPage).isEqualTo(8)

    // 현재 페이지 8, 페이지에 보여줄 컨텐츠 사이즈 1, 빠른 이동 시 이동할 페이지 단위 3
    val pagination4 = Pagination(8, 1, 3)

    val paging4 = createPaging(pagination4)

    assertThat(paging4.fastBackwardPage).isEqualTo(5)
    assertThat(paging4.fastForwardPage).isEqualTo(10)
  }

  @Test
  fun `컨텐츠 타입 변환`() {
    val paging = createPaging(Pagination(1, 5))

    val converted = paging.map { it.subject }

    for ((index, subject) in converted.content.withIndex()) {
      assertThat(subject).isEqualTo("제목$index")
    }
  }
}
