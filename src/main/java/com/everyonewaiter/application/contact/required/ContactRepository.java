package com.everyonewaiter.application.contact.required;

import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.Paging;

public interface ContactRepository {

  boolean existsUncompletedByNameOrLicense(String name, BusinessLicense license);

  Paging<Contact> findAllByAdmin(ContactAdminReadRequest readRequest);

  Contact findByIdOrThrow(Long contactId);

  Contact save(Contact contact);

}
