package com.everyonewaiter.application.store.request;

import com.everyonewaiter.global.support.Pagination;

public record RegistrationPage(Pagination pagination) {

  public RegistrationPage(long page, long size) {
    this(new Pagination(page, size));
  }

}
