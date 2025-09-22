package com.everyonewaiter.application.contact;

import com.everyonewaiter.application.contact.provided.ContactFinder;
import com.everyonewaiter.application.contact.provided.ContactProcessor;
import com.everyonewaiter.application.contact.required.ContactRepository;
import com.everyonewaiter.domain.contact.AlreadyExistsUncompletedContactException;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactCreateRequest;
import com.everyonewaiter.domain.shared.BusinessLicense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class ContactModifyService implements ContactProcessor {

  private final ContactFinder contactFinder;
  private final ContactRepository contactRepository;

  @Override
  public Contact create(ContactCreateRequest createRequest) {
    checkExistsUncompletedContact(createRequest);

    Contact contact = Contact.create(createRequest);

    return contactRepository.save(contact);
  }

  @Override
  public Contact processing(Long contactId) {
    Contact contact = contactFinder.findOrThrow(contactId);

    contact.processing();

    return contactRepository.save(contact);
  }

  @Override
  public Contact complete(Long contactId) {
    Contact contact = contactFinder.findOrThrow(contactId);

    contact.complete();

    return contactRepository.save(contact);
  }

  private void checkExistsUncompletedContact(ContactCreateRequest createRequest) {
    String storeName = createRequest.storeName();
    String license = createRequest.license();

    if (contactRepository.existsUncompleted(storeName, new BusinessLicense(license))) {
      throw new AlreadyExistsUncompletedContactException();
    }
  }

}
