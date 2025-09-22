package com.everyonewaiter.domain.contact;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Contact extends AggregateRootEntity<Contact> {

  private String storeName;

  private BusinessLicense license;

  private PhoneNumber phoneNumber;

  private ContactState state;

  public static Contact create(ContactCreateRequest createRequest) {
    Contact contact = new Contact();

    contact.storeName = requireNonNull(createRequest.storeName());
    contact.license = new BusinessLicense(createRequest.license());
    contact.phoneNumber = new PhoneNumber(createRequest.phoneNumber());
    contact.state = ContactState.PENDING;

    contact.registerEvent(new ContactCreateEvent(contact));

    return contact;
  }

  public void processing() {
    state(this.state == ContactState.PENDING, "접수 대기 중인 서비스 도입 문의가 아닙니다.");

    this.state = ContactState.PROCESSING;
  }

  public void complete() {
    state(this.state != ContactState.COMPLETE, "이미 처리가 완료된 서비스 도입 문의 입니다.");

    this.state = ContactState.COMPLETE;
  }

}
