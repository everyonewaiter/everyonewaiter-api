package com.everyonewaiter;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class EveryonewaiterApiApplicationTest {

  @Test
  void run() {
    try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
      EveryonewaiterApiApplication.main(new String[0]);

      mocked.verify(() -> SpringApplication.run(EveryonewaiterApiApplication.class, new String[0]));
    }
  }

}
