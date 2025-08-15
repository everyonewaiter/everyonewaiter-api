package com.everyonewaiter.application.contact.required;

import static com.everyonewaiter.domain.contact.ContactFixture.createContactCreateRequest;
import static com.everyonewaiter.domain.contact.ContactState.COMPLETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactAdminReadRequest;
import com.everyonewaiter.domain.contact.ContactNotFoundException;
import com.everyonewaiter.domain.shared.Paging;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
class ContactRepositoryTest extends IntegrationTest {

  private final ContactRepository contactRepository;

  @Test
  void existsUncompleted() {
    Contact pendingContact = createContact();
    Contact completeContact = createCompleteContact();

    boolean exists1 = contactRepository
        .existsUncompleted(completeContact.getStoreName(), completeContact.getLicense());
    boolean exists2 = contactRepository
        .existsUncompleted(pendingContact.getStoreName(), completeContact.getLicense());
    boolean exists3 = contactRepository
        .existsUncompleted(completeContact.getStoreName(), pendingContact.getLicense());

    assertThat(exists1).isFalse();
    assertThat(exists2).isTrue();
    assertThat(exists3).isTrue();
  }

  @Test
  void findAllByAdmin() {
    Contact contact = createContact();

    Paging<Contact> pagedContacts = findAllByAdmin(new ContactAdminReadRequest());

    assertThat(pagedContacts.getContent()).hasSize(1);
    assertThat(pagedContacts.getContent().getFirst().getId()).isEqualTo(contact.getId());
  }

  @Test
  void findAllByAdminWhereName() {
    Contact contact = createContact();

    Paging<Contact> pagedContacts = findAllByAdmin(
        new ContactAdminReadRequest(contact.getStoreName(), null, null, null, 1, 20)
    );

    assertThat(pagedContacts.getContent()).hasSize(1);
    assertThat(pagedContacts.getContent().getFirst().getId()).isEqualTo(contact.getId());

    Paging<Contact> emptyContacts = findAllByAdmin(
        new ContactAdminReadRequest("나루", null, null, null, 1, 20)
    );

    assertThat(emptyContacts.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWherePhoneNumber() {
    Contact contact = createContact();

    Paging<Contact> pagedContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, contact.getPhoneNumber().value(), null, null, 1, 20)
    );

    assertThat(pagedContacts.getContent()).hasSize(1);
    assertThat(pagedContacts.getContent().getFirst().getId()).isEqualTo(contact.getId());

    Paging<Contact> emptyContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, "01012345678", null, null, 1, 20)
    );

    assertThat(emptyContacts.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWhereLicense() {
    Contact contact = createContact();

    Paging<Contact> pagedContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, null, contact.getLicense().value(), null, 1, 20)
    );

    assertThat(pagedContacts.getContent()).hasSize(1);
    assertThat(pagedContacts.getContent().getFirst().getId()).isEqualTo(contact.getId());

    Paging<Contact> emptyContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, null, "123-12-12345", null, 1, 20)
    );

    assertThat(emptyContacts.getContent()).isEmpty();
  }

  @Test
  void findAllByAdminWhereState() {
    Contact contact = createContact();

    Paging<Contact> pagedContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, null, null, contact.getState(), 1, 20)
    );

    assertThat(pagedContacts.getContent()).hasSize(1);
    assertThat(pagedContacts.getContent().getFirst().getId()).isEqualTo(contact.getId());

    Paging<Contact> emptyContacts = findAllByAdmin(
        new ContactAdminReadRequest(null, null, null, COMPLETE, 1, 20)
    );

    assertThat(emptyContacts.getContent()).isEmpty();
  }

  private Paging<Contact> findAllByAdmin(ContactAdminReadRequest readRequest) {
    return contactRepository.findAllByAdmin(readRequest);
  }

  @Test
  void findByIdOrThrow() {
    Contact contact = createContact();

    Contact found = contactRepository.findByIdOrThrow(contact.getId());

    assertThat(found.getId()).isNotNull();
    assertThat(found.getId()).isEqualTo(contact.getId());
  }

  @Test
  void findByIdOrThrowFail() {
    assertThatThrownBy(() -> contactRepository.findByIdOrThrow(999L))
        .isInstanceOf(ContactNotFoundException.class);
  }

  private Contact createContact() {
    Contact contact = Contact.create(createContactCreateRequest());

    return contactRepository.save(contact);
  }

  private Contact createCompleteContact() {
    Contact contact = Contact.create(createContactCreateRequest("나루", "123-45-67890"));

    ReflectionTestUtils.setField(contact, "state", COMPLETE);

    return contactRepository.save(contact);
  }

}
