package com.everyonewaiter.domain.waiting.event;

public record WaitingCancelByStoreEvent(
    Long storeId,
    String storeName,
    String phoneNumber,
    int number
) {

}
