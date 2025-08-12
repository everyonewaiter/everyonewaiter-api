package com.everyonewaiter.application.auth.required;

import com.everyonewaiter.domain.auth.Auth;

public interface AuthRepository {

  boolean exists(Auth auth);

  int find(Auth auth);

  void save(Auth auth);

  void increment(Auth auth);

  void delete(Auth auth);

}
