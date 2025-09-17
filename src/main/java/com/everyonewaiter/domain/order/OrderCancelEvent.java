package com.everyonewaiter.domain.order;

public record OrderCancelEvent(Long orderId, Long storeId, int tableNo) {

}
