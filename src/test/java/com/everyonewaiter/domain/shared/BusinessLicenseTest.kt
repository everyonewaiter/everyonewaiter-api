package com.everyonewaiter.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BusinessLicenseTest {

  @Test
  void constructor() {
    String value = "443-60-00875";

    BusinessLicense businessLicense = new BusinessLicense(value);

    assertThat(businessLicense.value()).isEqualTo(value);
  }

  @Test
  void constructorFail() {
    assertThatThrownBy(() -> new BusinessLicense("4436000875"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
