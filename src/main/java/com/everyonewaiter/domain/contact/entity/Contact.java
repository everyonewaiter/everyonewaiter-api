package com.everyonewaiter.domain.contact.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.contact.event.ContactCreateEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "contact")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact extends AggregateRootEntity<Contact> {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "phone_number", nullable = false, updatable = false)
  private String phoneNumber;

  @Column(name = "license", nullable = false)
  private String license;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  public static Contact create(String name, String phoneNumber, String license) {
    Contact contact = new Contact();
    contact.name = name;
    contact.phoneNumber = phoneNumber;
    contact.license = license;
    contact.registerEvent(new ContactCreateEvent(name, phoneNumber, license));
    return contact;
  }

  public void complete() {
    this.active = false;
  }

}
