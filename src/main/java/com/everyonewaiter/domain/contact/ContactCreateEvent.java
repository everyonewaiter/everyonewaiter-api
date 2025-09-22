package com.everyonewaiter.domain.contact;

import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.PhoneNumber;

public record ContactCreateEvent(
    String storeName,
    PhoneNumber phoneNumber,
    BusinessLicense license
) {

  public ContactCreateEvent(Contact contact) {
    this(contact.getStoreName(), contact.getPhoneNumber(), contact.getLicense());
  }

}
