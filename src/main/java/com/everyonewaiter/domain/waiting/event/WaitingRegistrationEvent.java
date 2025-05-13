package com.everyonewaiter.domain.waiting.event;

public record WaitingRegistrationEvent(
    Long storeId,
    String storeName,
    String storeLandline,
    String phoneNumber,
    int adult,
    int infant,
    int number,
    String accessKey
) {

}
