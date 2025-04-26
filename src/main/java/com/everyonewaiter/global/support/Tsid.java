package com.everyonewaiter.global.support;

import com.github.f4b6a3.tsid.TsidCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Tsid {

  public static long nextLong() {
    return TsidCreator.getTsid().toLong();
  }

}
