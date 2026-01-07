package com.everyonewaiter.domain.support

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WordCounterTest {

  @Test
  fun `단어 개수 찾기`() {
    val content = "%s/menus/preview?storeId=%s"

    val count = WordCounter.count("%s", content)

    assertThat(count).isEqualTo(2)
  }
}
