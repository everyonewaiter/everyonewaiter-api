package com.everyonewaiter.application.store.provided;

public interface StoreValidator {

  void checkExists(Long storeId);

  void checkExists(Long storeId, Long accountId);

  void checkIsOpened(Long storeId);

  void checkPossibleClose(Long storeId);

}
