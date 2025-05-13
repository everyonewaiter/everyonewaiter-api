package com.everyonewaiter.application.waiting.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingWrite {

  public record Create(String phoneNumber, int adult, int infant) {

  }

}
