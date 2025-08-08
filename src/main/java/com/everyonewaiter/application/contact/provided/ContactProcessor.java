package com.everyonewaiter.application.contact.provided;

import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactCreateRequest;
import jakarta.validation.Valid;

public interface ContactProcessor {

  Contact create(@Valid ContactCreateRequest createRequest);

  Contact processing(Long contactId);

  Contact complete(Long contactId);

}
