package com.everyonewaiter.application.receipt.required;

public interface ReceiptPrintNoRepository {

  int get(Long storeId);

  void increment(Long storeId);

  void delete(Long storeId);

}
