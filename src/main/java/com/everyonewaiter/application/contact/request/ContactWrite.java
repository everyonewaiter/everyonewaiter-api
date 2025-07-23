package com.everyonewaiter.application.contact.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactWrite {

  public record Create(String name, String phoneNumber, String license) {

  }

}
