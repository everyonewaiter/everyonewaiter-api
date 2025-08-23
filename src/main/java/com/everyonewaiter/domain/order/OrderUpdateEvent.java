package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.receipt.Receipt;

public record OrderUpdateEvent(Long storeId, int tableNo, Receipt receipt) {

}
