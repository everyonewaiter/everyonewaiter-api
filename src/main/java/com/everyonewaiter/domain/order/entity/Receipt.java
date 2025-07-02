package com.everyonewaiter.domain.order.entity;

import java.util.List;

public record Receipt(String memo, int printNo, List<ReceiptMenu> receiptMenus) {

}
