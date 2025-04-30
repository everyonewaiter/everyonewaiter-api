package com.everyonewaiter.application.store.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationAdminWrite {

  public record Reject(String rejectReason) {

  }

}
