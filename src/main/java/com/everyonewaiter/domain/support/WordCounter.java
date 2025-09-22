package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class WordCounter {

  public static int count(String word, String content) {
    int count = 0;
    int index = 0;

    while ((index = content.indexOf(word, index)) != -1) {
      count++;
      index += word.length();
    }

    return count;
  }

}
