package com.everyonewaiter.domain.auth.repository;

import com.everyonewaiter.domain.auth.entity.Auth;

public interface AuthRepository {

  boolean exists(Auth auth);

  int find(Auth auth);

  void save(Auth auth);

  void increment(Auth auth);

  void delete(Auth auth);

}
