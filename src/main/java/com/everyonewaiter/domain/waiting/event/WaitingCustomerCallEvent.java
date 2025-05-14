package com.everyonewaiter.domain.waiting.event;

public record WaitingCustomerCallEvent(
    Long storeId,
    String storeName,
    String phoneNumber,
    int number,
    String accessKey
) {

}
