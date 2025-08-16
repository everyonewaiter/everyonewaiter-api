package com.everyonewaiter.domain.store.view;

import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.store.entity.Registration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationAdminView {

  public record Page(
      Long id,
      Long accountId,
      Email email,
      String name,
      Registration.Status status,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

  public record Detail(
      Long id,
      Long accountId,
      Email email,
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      String image,
      Registration.Status status,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

}
