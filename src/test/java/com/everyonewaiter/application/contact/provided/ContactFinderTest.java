package com.everyonewaiter.application.contact.provided;

import static com.everyonewaiter.domain.contact.ContactFixture.createContactCreateRequest;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.domain.contact.Contact;
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

    var response = contactFinder.findAllByAdmin(new ContactAdminReadRequest());

    assertThat(response.getContent()).hasSize(1);
    assertThat(response.getContent().getFirst().contactId())
        .isEqualTo(requireNonNull(contact.getId()).toString());
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
