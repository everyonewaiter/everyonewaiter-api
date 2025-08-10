package com.everyonewaiter.application.contact.provided;

import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.shared.Paging;
import jakarta.validation.Valid;

public interface ContactFinder {

  Paging<Contact> findAllByAdmin(@Valid ContactAdminReadRequest readRequest);

}
