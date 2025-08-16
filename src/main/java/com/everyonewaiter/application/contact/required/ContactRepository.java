package com.everyonewaiter.application.contact.required;

import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactAdminPageRequest;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.Paging;

public interface ContactRepository {

  boolean existsUncompleted(String storeName, BusinessLicense license);

  Paging<Contact> findAllByAdmin(ContactAdminPageRequest pageRequest);

  Contact findByIdOrThrow(Long contactId);

  Contact save(Contact contact);

}
