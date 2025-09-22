package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import com.github.f4b6a3.tsid.TsidCreator;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Tsid {

  public static long nextLong() {
    return TsidCreator.getTsid().toLong();
  }

  public static String nextString() {
    return TsidCreator.getTsid().toString();
  }

}
