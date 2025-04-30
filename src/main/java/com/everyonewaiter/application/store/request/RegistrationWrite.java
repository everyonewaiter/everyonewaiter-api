package com.everyonewaiter.application.store.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationWrite {

  public record Create(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      MultipartFile file
  ) {

  }

  public record Update(
      String name,
      String ceoName,
      String address,
      String landline,
      String license
  ) {

  }

  public record UpdateWithImage(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      MultipartFile file
  ) {

  }

}
