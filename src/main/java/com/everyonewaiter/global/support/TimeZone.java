package com.everyonewaiter.global.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeZone {
  UTC("UTC"),
  ASIA_SEOUL("Asia/Seoul"),
  ;

  private final String id;
}
