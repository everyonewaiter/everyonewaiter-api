package com.everyonewaiter.domain.contact.repository;

import com.everyonewaiter.domain.contact.entity.Contact;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import jakarta.annotation.Nullable;

public interface ContactRepository {

  boolean existsActiveByNameOrLicense(String name, String license);

  Paging<Contact> findAllByAdmin(
      @Nullable String name,
      @Nullable String phoneNumber,
      @Nullable String license,
      @Nullable Boolean active,
      Pagination pagination
  );

  Contact findByIdOrThrow(Long contactId);

  Contact save(Contact contact);

}
