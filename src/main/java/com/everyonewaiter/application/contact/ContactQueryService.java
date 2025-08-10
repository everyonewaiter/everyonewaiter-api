package com.everyonewaiter.application.contact;

import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.provided.ContactFinder;
import com.everyonewaiter.application.contact.required.ContactRepository;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.global.annotation.ReadOnlyTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class ContactQueryService implements ContactFinder {

  private final ContactRepository contactRepository;

  @Override
  @ReadOnlyTransactional
  public Paging<Contact> findAllByAdmin(ContactAdminReadRequest readRequest) {
    return contactRepository.findAllByAdmin(readRequest);
  }

}
