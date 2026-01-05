package com.everyonewaiter.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class JwtPayloadTest {

  @Test
  void constructor() {
    JwtPayload payload1 = new JwtPayload(1L, "1");
    JwtPayload payload2 = new JwtPayload(2L, 2L);

    assertThat(payload1.id()).isEqualTo(1L);
    assertThat(payload1.subject()).isEqualTo("1");
    assertThat(payload2.id()).isEqualTo(2L);
    assertThat(payload2.subject()).isEqualTo("2");
  }

  @Test
  void parseLongSubject() {
    JwtPayload payload = new JwtPayload(1L, "1");

    Long subject = payload.parseLongSubject();

    assertThat(subject).isEqualTo(1L);
  }

  @Test
  void parseLongSubjectFail() {
    JwtPayload payload = new JwtPayload(1L, "string");

    assertThatThrownBy(payload::parseLongSubject)
        .isInstanceOf(NumberFormatException.class);
  }

}
