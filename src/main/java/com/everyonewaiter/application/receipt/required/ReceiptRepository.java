package com.everyonewaiter.application.receipt.required;

public interface ReceiptRepository {

  int getPrintNo(Long storeId);

  void incrementPrintNo(Long storeId);

  void deletePrintNo(Long storeId);

}
