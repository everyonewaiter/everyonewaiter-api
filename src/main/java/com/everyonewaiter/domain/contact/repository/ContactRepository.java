package com.everyonewaiter.domain.contact.repository;

import com.everyonewaiter.domain.contact.entity.Contact;

public interface ContactRepository {

  boolean existsActiveByNameOrLicense(String name, String license);

  Contact save(Contact contact);

}
