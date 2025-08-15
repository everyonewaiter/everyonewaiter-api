package com.everyonewaiter;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

class DatabaseCleanUpExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) {
    DatabaseCleaner databaseCleaner = getApplicationContext(context).getBean(DatabaseCleaner.class);

    databaseCleaner.clean();
  }

}
