package com.everyonewaiter.domain.store;

public record RegistrationRejectEvent(Long accountId, String storeName, String rejectReason) {

  public RegistrationRejectEvent(Registration registration) {
    this(
        registration.getAccount().getId(),
        registration.getDetail().getName(),
        registration.getRejectReason()
    );
  }

}
