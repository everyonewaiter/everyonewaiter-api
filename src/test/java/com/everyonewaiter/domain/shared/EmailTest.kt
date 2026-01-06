package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void constructor() {
    String address = "admin@everyonewaiter.com";

    Email email = new Email(address);

    assertThat(email.address()).isEqualTo(address);
  }

  @Test
  void constructorFail() {
    assertThatThrownBy(() -> new Email("admin-everyonewaiter.com"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
