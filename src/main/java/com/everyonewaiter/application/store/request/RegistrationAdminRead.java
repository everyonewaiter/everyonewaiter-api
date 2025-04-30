package com.everyonewaiter.application.store.request;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.global.support.Pagination;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationAdminRead {

  public record PageView(
      @Nullable
      String email,

      @Nullable
      String name,

      @Nullable
      Registration.Status status,

      Pagination pagination
  ) {

    public PageView(
        @Nullable String email,
        @Nullable String name,
        @Nullable Registration.Status status,
        long page,
        long size
    ) {
      this(email, name, status, new Pagination(page, size));
    }

  }

}
