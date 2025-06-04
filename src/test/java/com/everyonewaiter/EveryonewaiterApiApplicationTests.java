package com.everyonewaiter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class EveryonewaiterApiApplicationTests {

  @Test
  void contextLoads() {
    // Spring boot application context loads successfully
  }

}
