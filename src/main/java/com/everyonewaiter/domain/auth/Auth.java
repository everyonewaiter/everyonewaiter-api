package com.everyonewaiter.domain.auth;

import java.time.Duration;

public interface Auth {

  Duration expiration();

  String key();

  default int value() {
    return -2;
  }

}
