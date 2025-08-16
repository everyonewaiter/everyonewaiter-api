package com.everyonewaiter.domain.store;

public record RegistrationReapplyEvent(String storeName, String rejectReason) {

  public RegistrationReapplyEvent(BusinessDetail businessDetail, String rejectReason) {
    this(businessDetail.getName(), rejectReason);
  }

}
