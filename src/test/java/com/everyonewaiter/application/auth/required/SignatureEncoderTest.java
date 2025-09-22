package com.everyonewaiter.application.auth.required;

import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class SignatureEncoderTest extends IntegrationTest {

  private final SignatureEncoder signatureEncoder;

  @Test
  void signatureEncoder() {
    String encoded = signatureEncoder.encode("awesome", "secret");

    assertThat(signatureEncoder.matches(encoded, "awesome", "secret")).isTrue();
  }

}
