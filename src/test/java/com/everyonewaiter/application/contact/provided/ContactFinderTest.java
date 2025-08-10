package com.everyonewaiter.application.contact.provided;

import static com.everyonewaiter.domain.contact.ContactFixture.createContactCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.shared.Paging;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

@IntegrationTest
record ContactFinderTest(
    EntityManager entityManager,
    ContactFinder contactFinder,
    ContactProcessor contactProcessor
) {

  @Test
  void findAllByAdmin() {
    Contact contact = contactProcessor.create(createContactCreateRequest());

    entityManager.flush();
    entityManager.clear();

    Paging<Contact> contacts = contactFinder.findAllByAdmin(new ContactAdminReadRequest());

    assertThat(contacts.getContent()).hasSize(1);
    assertThat(contacts.getContent().getFirst().getId()).isEqualTo(contact.getId());
  }

  @Test
  void readAdminRequestFail() {
    checkValidation(new ContactAdminReadRequest(null, null, null, null, 0, 1));
    checkValidation(new ContactAdminReadRequest(null, null, null, null, 1, 0));
  }

  private void checkValidation(ContactAdminReadRequest readRequest) {
    assertThatThrownBy(() -> contactFinder.findAllByAdmin(readRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
