package com.everyonewaiter.domain.health;

public class ApkVersionFixture {

  public static ApkVersionCreateRequest createRequest() {
    return new ApkVersionCreateRequest(1, 0, 0, "https://cdn.everyonewaiter.com");
  }

}
