package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PhoneNumberTest {

  @Test
  void constructor() {
    String value = "01012345678";

    PhoneNumber phoneNumber = new PhoneNumber(value);

    assertThat(phoneNumber.value()).isEqualTo(value);
  }

  @Test
  void constructorFail() {
    assertThatThrownBy(() -> new PhoneNumber("010-1234-5678"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
