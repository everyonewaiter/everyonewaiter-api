package com.everyonewaiter.application.auth.required;

import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.auth.JwtPayload;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class JwtProviderTest extends IntegrationTest {

  private final JwtProvider jwtProvider;

  @Test
  void jwtProvider() {
    JwtPayload payload = new JwtPayload(1L, "1");

    String encoded = jwtProvider.encode(payload, Duration.ofMinutes(5));

    Optional<JwtPayload> decoded = jwtProvider.decode(encoded);
    assertThat(decoded).isPresent();
    assertThat(decoded.get().id()).isEqualTo(1L);
    assertThat(decoded.get().subject()).isEqualTo("1");
  }

}
