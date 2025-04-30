package com.everyonewaiter.domain.store.event;

public record RegistrationRejectEvent(Long accountId, String storeName, String rejectReason) {

}
