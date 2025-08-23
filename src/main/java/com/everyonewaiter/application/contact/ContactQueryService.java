package com.everyonewaiter.application.contact;

import com.everyonewaiter.application.contact.provided.ContactFinder;
import com.everyonewaiter.application.contact.required.ContactRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactAdminPageRequest;
import com.everyonewaiter.domain.shared.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class ContactQueryService implements ContactFinder {

  private final ContactRepository contactRepository;

  @Override
  public Paging<Contact> findAllByAdmin(ContactAdminPageRequest pageRequest) {
    return contactRepository.findAll(pageRequest);
  }

  @Override
  public Contact findOrThrow(Long contactId) {
    return contactRepository.findOrThrow(contactId);
  }

}
