package com.everyonewaiter.domain.order.repository;

public interface ReceiptRepository {

  int getPrintNo(Long storeId);

  void incrementPrintNo(Long storeId);

}
