package com.everyonewaiter.application.contact.provided;

import static com.everyonewaiter.domain.contact.ContactFixture.createContactCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.contact.AlreadyExistsUncompletedContactException;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactCreateRequest;
import com.everyonewaiter.domain.contact.ContactState;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

@IntegrationTest
record ContactProcessorTest(EntityManager entityManager, ContactProcessor contactProcessor) {

  @Test
  void create() {
    Contact contact = createContact();

    assertThat(contact.getId()).isNotNull();
  }

  @Test
  void existsUncompletedContact() {
    createContact();

    assertThatThrownBy(this::createContact)
        .isInstanceOf(AlreadyExistsUncompletedContactException.class);
  }

  @Test
  void processing() {
    Contact contact = createContact();

    contact = contactProcessor.processing(contact.getId());
    entityManager.flush();

    assertThat(contact.getState()).isEqualTo(ContactState.PROCESSING);
  }

  @Test
  void complete() {
    Contact contact = createContact();

    contact = contactProcessor.complete(contact.getId());
    entityManager.flush();

    assertThat(contact.getState()).isEqualTo(ContactState.COMPLETE);
  }

  private Contact createContact() {
    Contact contact = contactProcessor.create(createContactCreateRequest());

    entityManager.flush();
    entityManager.clear();

    return contact;
  }

  @Test
  void createContactRequestFail() {
    checkValidation(new ContactCreateRequest("", "01044591812", "443-60-00875"));
    checkValidation(new ContactCreateRequest("나루", "010-4459-1812", "443-60-00875"));
    checkValidation(new ContactCreateRequest("나루", "01044591812", "4436000875"));
  }

  private void checkValidation(ContactCreateRequest createRequest) {
    assertThatThrownBy(() -> contactProcessor.create(createRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
