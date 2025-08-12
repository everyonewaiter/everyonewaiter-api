package com.everyonewaiter.domain.auth;

import java.time.Duration;

public interface Auth {

  String key();

  int value();

  Duration expiration();

}
