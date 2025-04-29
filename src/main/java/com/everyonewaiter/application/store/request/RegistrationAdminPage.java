package com.everyonewaiter.application.store.request;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.global.support.Pagination;
import jakarta.annotation.Nullable;

public record RegistrationAdminPage(
    @Nullable
    String email,

    @Nullable
    String name,

    @Nullable
    Registration.Status status,

    Pagination pagination
) {

  public RegistrationAdminPage(
      @Nullable String email,
      @Nullable String name,
      @Nullable Registration.Status status,
      long page,
      long size
  ) {
    this(email, name, status, new Pagination(page, size));
  }

}
