package com.everyonewaiter.application.contact.request;

import com.everyonewaiter.global.support.Pagination;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactAdminRead {

  public record PageView(
      @Nullable
      String name,

      @Nullable
      String phoneNumber,

      @Nullable
      String license,

      @Nullable
      Boolean active,

      Pagination pagination
  ) {

    public PageView(
        @Nullable String name,
        @Nullable String phoneNumber,
        @Nullable String license,
        @Nullable Boolean active,
        long page,
        long size
    ) {
      this(name, phoneNumber, license, active, new Pagination(page, size));
    }

  }

}
