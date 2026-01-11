package com.everyonewaiter.application.receipt.provided;

import com.everyonewaiter.domain.receipt.Receipt;
import java.util.List;

public interface ReceiptCreator {

  Receipt create(Long storeId, int tableNo, Long orderId);

  Receipt create(Long storeId, int tableNo, List<Long> orderIds);

  Receipt createCancel(Long storeId, int tableNo, Long orderId);

  Receipt copyWithIncrementPrintNo(Long storeId, Receipt receipt);

}
