package com.everyonewaiter.application.contact;

import com.everyonewaiter.application.contact.request.ContactWrite;
import com.everyonewaiter.domain.contact.entity.Contact;
import com.everyonewaiter.domain.contact.repository.ContactRepository;
import com.everyonewaiter.domain.contact.service.ContactValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

  private final ContactValidator contactValidator;
  private final ContactRepository contactRepository;

  @Transactional
  public Long create(ContactWrite.Create request) {
    contactValidator.validateUnique(request.name(), request.license());
    Contact contact = Contact.create(request.name(), request.phoneNumber(), request.license());
    return contactRepository.save(contact).getId();
  }

  @Transactional
  public void complete(Long contactId) {
    Contact contact = contactRepository.findByIdOrThrow(contactId);
    contact.complete();
    contactRepository.save(contact);
  }

}
