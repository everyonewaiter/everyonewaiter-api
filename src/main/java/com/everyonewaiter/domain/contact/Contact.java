package com.everyonewaiter.domain.contact;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "contact",
    indexes = {
        @Index(name = "idx_contact_phone_number", columnList = "phone_number"),
        @Index(name = "idx_contact_license_phone_number", columnList = "license, phone_number"),
        @Index(name = "idx_contact_store_name_phone_number", columnList = "store_name, phone_number"),
        @Index(name = "idx_contact_store_name_license_phone_number", columnList = "store_name, license, phone_number")
    }
)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Contact extends AggregateRootEntity<Contact> {

  @Column(name = "store_name", nullable = false, length = 30)
  private String storeName;

  @Embedded
  private BusinessLicense license;

  @Embedded
  private PhoneNumber phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
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
