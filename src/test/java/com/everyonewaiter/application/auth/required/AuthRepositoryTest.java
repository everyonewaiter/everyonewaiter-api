package com.everyonewaiter.application.auth.required;

import static com.everyonewaiter.domain.auth.AuthFixture.createAuthAttempt;
import static com.everyonewaiter.domain.auth.AuthFixture.createAuthCode;
import static com.everyonewaiter.domain.auth.AuthFixture.createAuthSuccess;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.auth.AuthAttempt;
import com.everyonewaiter.domain.auth.AuthCode;
import com.everyonewaiter.domain.auth.AuthSuccess;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
class AuthRepositoryTest extends IntegrationTest {

  private final AuthRepository authRepository;
  private final RedisTemplate<String, String> redisTemplate;

  @Test
  void exists() {
    AuthCode authCode = createAuthCode();

    assertThat(authRepository.exists(authCode)).isFalse();

    authRepository.save(authCode);

    assertThat(authRepository.exists(authCode)).isTrue();
  }

  @Test
  void find() {
    AuthCode authCode = createAuthCode();

    assertThat(authRepository.find(authCode)).isZero();

    authRepository.save(authCode);

    assertThat(authRepository.find(authCode)).isEqualTo(123456);
  }

  @Test
  void increment() {
    AuthAttempt authAttempt = createAuthAttempt();

    assertThat(redisTemplate.getExpire(authAttempt.key())).isEqualTo(-2);

    authRepository.increment(authAttempt);

    assertThat(authRepository.find(authAttempt)).isEqualTo(1);
    assertThat(redisTemplate.getExpire(authAttempt.key())).isNotEqualTo(-2);

    authRepository.increment(authAttempt);

    assertThat(authRepository.find(authAttempt)).isEqualTo(2);
  }

  @Test
  void delete() {
    AuthSuccess authSuccess = createAuthSuccess();

    authRepository.save(authSuccess);

    assertThat(redisTemplate.hasKey(authSuccess.key())).isTrue();

    authRepository.delete(authSuccess);

    assertThat(redisTemplate.hasKey(authSuccess.key())).isFalse();
  }

}
