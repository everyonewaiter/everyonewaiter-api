package com.everyonewaiter.domain.contact;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ContactFixture {

  public static ContactCreateRequest createContactCreateRequest() {
    return createContactCreateRequest("홍길동식당", "443-60-00875");
  }

  public static ContactCreateRequest createContactCreateRequest(String name, String license) {
    return new ContactCreateRequest(name, "01044591812", license);
  }

}
