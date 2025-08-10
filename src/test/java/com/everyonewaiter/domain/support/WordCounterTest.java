package com.everyonewaiter.domain.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WordCounterTest {

  @Test
  void count() {
    String content = "%s/menus/preview?storeId=%s";

    int count = WordCounter.count("%s", content);

    assertThat(count).isEqualTo(2);
  }

}
