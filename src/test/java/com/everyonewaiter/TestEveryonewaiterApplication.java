package com.everyonewaiter;

import org.springframework.boot.SpringApplication;

public class TestEveryonewaiterApplication {

  public static void main(String[] args) {
    SpringApplication
        .from(EveryonewaiterApiApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }

}
