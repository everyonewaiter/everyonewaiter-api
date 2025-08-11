package com.everyonewaiter.domain.health;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ApkVersionFixture {

  public static ApkVersionCreateRequest createApkVersionCreateRequest() {
    return new ApkVersionCreateRequest(1, 0, 0, "https://cdn.everyonewaiter.com/release.apk");
  }

}
