package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PagingTest {

  Pagination firstPage = new Pagination(1, 10);
  Pagination secondPage = new Pagination(2, 10);
  Pagination tenthPage = new Pagination(10, 10);

  @Test
  void hasNextShouldBeTrue() {
    List<Integer> content = createDummyContent(0, 11);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.hasNext()).isTrue();
  }

  @Test
  void hasNextShouldBeFalse() {
    List<Integer> content = createDummyContent(0, 10);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.hasNext()).isFalse();
  }

  @Test
  void hasPreviousShouldBeTrue() {
    List<Integer> content = createDummyContent(0, 11);

    Paging<Integer> paging = new Paging<>(content, content.size(), secondPage);

    assertThat(paging.hasPrevious()).isTrue();
  }

  @Test
  void hasPreviousShouldBeFalse() {
    List<Integer> content = createDummyContent(0, 10);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.hasPrevious()).isFalse();
  }

  @Test
  void isFirstShouldBeTrue() {
    List<Integer> content = createDummyContent(0, 10);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.isFirst()).isTrue();
  }

  @Test
  void isFirstShouldBeFalse() {
    List<Integer> content = createDummyContent(0, 11);

    Paging<Integer> paging = new Paging<>(content, content.size(), secondPage);

    assertThat(paging.isFirst()).isFalse();
  }

  @Test
  void isLastShouldBeTrue() {
    List<Integer> content = createDummyContent(0, 10);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.isLast()).isTrue();
  }

  @Test
  void isLastShouldBeFalse() {
    List<Integer> content = createDummyContent(0, 11);

    Paging<Integer> paging = new Paging<>(content, content.size(), firstPage);

    assertThat(paging.isLast()).isFalse();
  }

  @Test
  void fastForwardPage() {
    List<Integer> content1 = createDummyContent(0, 30);
    List<Integer> content2 = createDummyContent(0, 100);

    Paging<Integer> paging1 = new Paging<>(content1, content1.size(), firstPage);
    Paging<Integer> paging2 = new Paging<>(content2, content2.size(), secondPage);

    assertThat(paging1.getFastForwardPage()).isEqualTo(3);
    assertThat(paging2.getFastForwardPage()).isEqualTo(7);
  }

  @Test
  void fastBackwardPage() {
    List<Integer> content1 = createDummyContent(0, 10);
    List<Integer> content2 = createDummyContent(0, 100);

    Paging<Integer> paging1 = new Paging<>(content1, content1.size(), firstPage);
    Paging<Integer> paging2 = new Paging<>(content2, content2.size(), tenthPage);

    assertThat(paging1.getFastBackwardPage()).isEqualTo(1);
    assertThat(paging2.getFastBackwardPage()).isEqualTo(5);
  }

  @Test
  void map() {
    List<String> content = createDummyContent("1", 10);
    Paging<String> paging = new Paging<>(content, content.size(), firstPage);

    Paging<Integer> mappedPaging = paging.map(Integer::parseInt);

    for (Integer i : mappedPaging.getContent()) {
      assertThat(i).isEqualTo(1);
    }
  }

  private <T> List<T> createDummyContent(T content, long size) {
    List<T> contents = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      contents.add(content);
    }

    return Collections.unmodifiableList(contents);
  }

}
