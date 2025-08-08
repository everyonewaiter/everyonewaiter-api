package com.everyonewaiter.domain.contact;

import static com.everyonewaiter.domain.contact.ContactFixture.createContactCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ContactTest {

  @Test
  void create() {
    Contact contact = Contact.create(createContactCreateRequest());

    assertThat(contact.getState()).isEqualTo(ContactState.PENDING);

    Object domainEvents = ReflectionTestUtils.invokeGetterMethod(contact, "domainEvents");
    assertThat(domainEvents).isInstanceOf(List.class);
    assertThat((List<?>) domainEvents).hasSize(1);
  }

  @Test
  void processing() {
    Contact contact = Contact.create(createContactCreateRequest());

    contact.processing();

    assertThat(contact.getState()).isEqualTo(ContactState.PROCESSING);
  }

  @Test
  void processingFail() {
    Contact contact = Contact.create(createContactCreateRequest());
    contact.processing();

    assertThatThrownBy(contact::processing)
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void complete() {
    Contact contact = Contact.create(createContactCreateRequest());
    contact.processing();

    contact.complete();

    assertThat(contact.getState()).isEqualTo(ContactState.COMPLETE);
  }

  @Test
  void completeFail() {
    Contact contact = Contact.create(createContactCreateRequest());
    contact.processing();
    contact.complete();

    assertThatThrownBy(contact::complete)
        .isInstanceOf(IllegalStateException.class);
  }

}
