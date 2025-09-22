package com.everyonewaiter.domain.receipt;

import java.util.List;

public record ReceiptResendEvent(Long storeId, int tableNo, List<Long> orderIds) {

}
