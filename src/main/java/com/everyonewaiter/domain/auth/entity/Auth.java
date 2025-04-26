package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;

public interface Auth {

  String getKey();

  int getValue();

  Duration getExpiration();

}
