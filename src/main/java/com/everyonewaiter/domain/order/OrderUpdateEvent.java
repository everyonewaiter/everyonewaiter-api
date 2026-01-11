package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.receipt.Receipt;
import org.jspecify.annotations.Nullable;

public record OrderUpdateEvent(Long storeId, int tableNo, @Nullable Receipt diff) {

}
