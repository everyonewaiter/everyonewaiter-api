package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PaginationTest {

  @Test
  void countLimit() {
    Pagination pagination1 = new Pagination(1, 20, 5);
    Pagination pagination2 = new Pagination(2, 20, 10);

    assertThat(pagination1.countLimit()).isEqualTo(101);
    assertThat(pagination2.countLimit()).isEqualTo(221);
  }

  @Test
  void limit() {
    Pagination pagination1 = new Pagination(1, 10);
    Pagination pagination2 = new Pagination(1, 20);

    assertThat(pagination1.limit()).isEqualTo(10);
    assertThat(pagination2.limit()).isEqualTo(20);
  }

  @Test
  void offset() {
    Pagination pagination1 = new Pagination(1, 10);
    Pagination pagination2 = new Pagination(2, 10);
    Pagination pagination3 = new Pagination(5, 20);

    assertThat(pagination1.offset()).isZero();
    assertThat(pagination2.offset()).isEqualTo(10);
    assertThat(pagination3.offset()).isEqualTo(80);
  }

}
