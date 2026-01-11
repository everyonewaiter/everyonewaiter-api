package com.everyonewaiter.domain.order;

import java.util.List;

public record OrderUpdateEvent(
    Long storeId,
    int tableNo,
    List<Long> orderIds,
    OrderUpdateRequests updateRequests
) {

}
