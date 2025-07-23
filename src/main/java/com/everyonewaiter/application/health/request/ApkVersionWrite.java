package com.everyonewaiter.application.health.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApkVersionWrite {

  public record Create(int majorVersion, int minorVersion, int patchVersion, String downloadUrl) {

  }

}
