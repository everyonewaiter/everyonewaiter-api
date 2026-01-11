package com.everyonewaiter.adapter.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class DataInitializerTransactionSupporter {

  @Transactional
  public void executeInTransaction(Runnable runnable) {
    runnable.run();
  }

}
