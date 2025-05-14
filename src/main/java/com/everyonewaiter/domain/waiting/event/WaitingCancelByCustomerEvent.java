package com.everyonewaiter.domain.waiting.event;

public record WaitingCancelByCustomerEvent(
    Long storeId,
    String storeName,
    String phoneNumber,
    int number
) {

}
